package yaboichips.rogue_planets.events;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yaboichips.rogue_planets.RoguePlanets;
import yaboichips.rogue_planets.capabilties.player.PlayerDataUtils;
import yaboichips.rogue_planets.core.RPEntities;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Spawns waves of hostile mobs while a player is inside one of the dynamic
 * planet dimensions created by the Infiniverse API (see {@code CanonEntity#teleportToLevel}).
 * Wave intensity scales with how little O2 the player has left and how deep underground they are.
 */
@Mod.EventBusSubscriber(modid = RoguePlanets.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WaveSpawnEvents {

    // Matches the O2 value CanonEntity grants on entry into a planet dimension - used as the "full tank" reference for intensity.
    private static final int MAX_O2 = 19000;
    // Matches miner_dimension's min_y / bedrock ceiling - used as the depth reference for intensity.
    private static final int DEEP_Y = -64;
    private static final int SURFACE_Y = 128;

    private static final long MIN_WAVE_INTERVAL_TICKS = 200;  // 10s at max intensity
    private static final long MAX_WAVE_INTERVAL_TICKS = 600;  // 30s at min intensity

    private static final int MIN_WAVE_SIZE = 1;
    private static final int MAX_WAVE_SIZE = 12;
    private static final int MAX_ALIVE_PER_PLAYER = 24;

    private static final int MIN_SPAWN_RADIUS = 5;
    private static final int MAX_SPAWN_RADIUS = 16;
    private static final int VERTICAL_SEARCH = 6;
    private static final int SPAWN_ATTEMPTS = 10;

    public static final List<Supplier<EntityType<? extends Mob>>> COMMON_MOBS = List.of(
            () -> EntityType.ZOMBIE
    );

    public static final List<Supplier<EntityType<? extends Mob>>> UNCOMMON_MOBS = List.of(
            RPEntities.ALIEN::get
    );

    public static final List<Supplier<EntityType<? extends Mob>>> RARE_MOBS = List.of(
            RPEntities.CYCLOPS::get
    );

    public static final List<Supplier<EntityType<? extends Mob>>> BOSS_MOBS = List.of(
            () -> EntityType.RAVAGER
    );

    private static final Map<UUID, Long> nextWaveTick = new ConcurrentHashMap<>();
    private static final Map<UUID, Set<UUID>> aliveWaveMobs = new ConcurrentHashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        if (event.side.isClient()) return;
        if (!(event.player instanceof ServerPlayer player)) return;

        UUID uuid = player.getUUID();

        if (!isInPlanetDimension(player)) {
            nextWaveTick.remove(uuid);
            aliveWaveMobs.remove(uuid);
            return;
        }

        long dueTick = nextWaveTick.computeIfAbsent(uuid, id -> RoguePlanets.currentTick);
        if (RoguePlanets.currentTick < dueTick) return;

        float intensity = computeIntensity(player);
        spawnWave(player, intensity);

        long interval = MAX_WAVE_INTERVAL_TICKS - Math.round((MAX_WAVE_INTERVAL_TICKS - MIN_WAVE_INTERVAL_TICKS) * intensity);
        nextWaveTick.put(uuid, RoguePlanets.currentTick + interval);
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        UUID uuid = event.getEntity().getUUID();
        nextWaveTick.remove(uuid);
        aliveWaveMobs.remove(uuid);
    }

    private static boolean isInPlanetDimension(ServerPlayer player) {
        var location = player.serverLevel().dimension().location();
        return location.getNamespace().equals(RoguePlanets.MODID) && location.getPath().contains("planet");
    }

    /**
     * 0 = full O2 and near the surface, 1 = O2 nearly empty and near the bottom of the dimension.
     */
    private static float computeIntensity(ServerPlayer player) {
        int o2 = PlayerDataUtils.getO2(player);
        float o2Intensity = 1f - Mth.clamp(o2 / (float) MAX_O2, 0f, 1f);

        float depthIntensity = 1f - Mth.clamp((float) (player.getY() - DEEP_Y) / (SURFACE_Y - DEEP_Y), 0f, 1f);

        return Mth.clamp(o2Intensity * 0.5f + depthIntensity * 0.5f, 0f, 1f);
    }

    private static void spawnWave(ServerPlayer player, float intensity) {
        ServerLevel level = player.serverLevel();
        UUID uuid = player.getUUID();
        Set<UUID> alive = aliveWaveMobs.computeIfAbsent(uuid, id -> new HashSet<>());
        alive.removeIf(mobId -> level.getEntity(mobId) == null);

        if (alive.size() >= MAX_ALIVE_PER_PLAYER) return;

        int waveSize = MIN_WAVE_SIZE + Math.round((MAX_WAVE_SIZE - MIN_WAVE_SIZE) * intensity);
        waveSize = Math.min(waveSize, MAX_ALIVE_PER_PLAYER - alive.size());

        RandomSource random = level.getRandom();
        boolean bossSpawned = false;

        for (int i = 0; i < waveSize; i++) {
            BlockPos pos = findSpawnPos(level, player, random);
            if (pos == null) continue;

            Tier tier = rollTier(random, intensity, bossSpawned);
            if (tier == Tier.BOSS) bossSpawned = true;

            EntityType<? extends Mob> type = pick(poolFor(tier), random);
            Mob mob = type.create(level);
            if (mob == null) continue;

            mob.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, random.nextFloat() * 360f, 0f);
            mob.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.EVENT, null, null);
            level.addFreshEntity(mob);
            alive.add(mob.getUUID());
        }
    }

    private enum Tier { COMMON, UNCOMMON, RARE, BOSS }

    private static Tier rollTier(RandomSource random, float intensity, boolean bossAlreadySpawned) {
        float bossChance = bossAlreadySpawned || BOSS_MOBS.isEmpty() ? 0f : Mth.clamp((intensity - 0.75f) * 0.6f, 0f, 0.15f);
        float rareChance = Mth.clamp(0.05f + intensity * 0.35f, 0.05f, 0.4f);
        float uncommonChance = Mth.clamp(0.20f + intensity * 0.15f, 0.20f, 0.35f);

        float roll = random.nextFloat();
        if (roll < bossChance) return Tier.BOSS;
        roll -= bossChance;
        if (roll < rareChance) return Tier.RARE;
        roll -= rareChance;
        if (roll < uncommonChance) return Tier.UNCOMMON;
        return Tier.COMMON;
    }

    private static List<Supplier<EntityType<? extends Mob>>> poolFor(Tier tier) {
        return switch (tier) {
            case BOSS -> BOSS_MOBS;
            case RARE -> RARE_MOBS;
            case UNCOMMON -> UNCOMMON_MOBS;
            case COMMON -> COMMON_MOBS;
        };
    }

    private static EntityType<? extends Mob> pick(List<Supplier<EntityType<? extends Mob>>> tier, RandomSource random) {
        return tier.get(random.nextInt(tier.size())).get();
    }

    private static BlockPos findSpawnPos(ServerLevel level, ServerPlayer player, RandomSource random) {
        for (int attempt = 0; attempt < SPAWN_ATTEMPTS; attempt++) {
            double angle = random.nextDouble() * Math.PI * 2;
            double distance = MIN_SPAWN_RADIUS + random.nextDouble() * (MAX_SPAWN_RADIUS - MIN_SPAWN_RADIUS);
            int x = Mth.floor(player.getX() + Math.cos(angle) * distance);
            int z = Mth.floor(player.getZ() + Math.sin(angle) * distance);
            int centerY = Mth.floor(player.getY());

            for (int dy = -VERTICAL_SEARCH; dy <= VERTICAL_SEARCH; dy++) {
                int y = centerY + dy;
                if (y <= level.getMinBuildHeight() + 1 || y >= level.getMaxBuildHeight() - 1) continue;

                BlockPos pos = new BlockPos(x, y, z);
                if (isValidSpawnPos(level, pos)) return pos;
            }
        }
        return null;
    }

    private static boolean isValidSpawnPos(ServerLevel level, BlockPos pos) {
        if (!level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()) return false;
        if (!level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()) return false;
        return !level.getBlockState(pos.below()).getCollisionShape(level, pos.below()).isEmpty();
    }
}
