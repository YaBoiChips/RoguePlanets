package yaboichips.rogue_planets.common.entities.canon;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.util.GeckoLibUtil;
import com.mojang.serialization.DynamicOps;
import net.commoble.infiniverse.api.InfiniverseAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import yaboichips.rogue_planets.RoguePlanets;
import yaboichips.rogue_planets.common.containers.PlanetInventoryContainer;
import yaboichips.rogue_planets.common.containers.SaveableSimpleContainer;
import yaboichips.rogue_planets.common.items.LevelableItem;
import yaboichips.rogue_planets.common.nbt.parties.PartyData;
import yaboichips.rogue_planets.core.RPEntities;
import yaboichips.rogue_planets.data.PlayerDataUtils;

import java.util.Optional;
import java.util.OptionalLong;
import java.util.Set;

import static yaboichips.rogue_planets.RoguePlanets.MODID;

public class CanonEntity extends Entity implements GeoEntity {
    private final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

    public CanonEntity(EntityType<? extends Entity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
    }

    @Override
    protected boolean canRide(Entity entity) {
        return true;
    }

    @Override
    public boolean canBeCollidedWith(Entity entity) {
        return false;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean hurtServer(ServerLevel level, net.minecraft.world.damagesource.DamageSource damageSource, float amount) {
        return false;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, net.minecraft.world.phys.Vec3 hitPos) {
        if (player instanceof ServerPlayer serverPlayer && !level().isClientSide()) {
            PartyData partyData = PartyData.get(serverPlayer.level());
            if (PlayerDataUtils.getIsInitiated(serverPlayer)) {
                if (partyData.isInParty(serverPlayer.getUUID())) {
                    if (partyData.isLeader(serverPlayer.getUUID())) {
                        partyData.getParty(serverPlayer.getUUID()).setLeaderInDimension(true);
                        scheduleLaunch(player);
                        return InteractionResult.SUCCESS;
                    } else if (partyData.getParty(serverPlayer.getUUID()).isLeaderInDimension()) {
                        scheduleLaunch(player);
                        return InteractionResult.SUCCESS;
                    } else {
                        player.sendSystemMessage(Component.literal("Party Leader must enter first"));
                        return InteractionResult.FAIL;
                    }
                } else {
                    scheduleLaunch(player);
                    return InteractionResult.SUCCESS;
                }
            } else {
                player.sendSystemMessage(Component.literal("Talk to the CEO First"));
                return InteractionResult.FAIL;
            }
        } else {
            return InteractionResult.FAIL;
        }
    }

    public void scheduleLaunch(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.startRiding(this);
            RoguePlanets.scheduleTask(27, player::stopRiding);
            RoguePlanets.scheduleTask(30, () -> launchPlayer(serverPlayer, true));
            RoguePlanets.scheduleTask(45, () -> launchPlayer(serverPlayer, false));
            RoguePlanets.scheduleTask(60, () -> teleportToLevel(serverPlayer));
        }
    }

    public void launchPlayer(ServerPlayer serverPlayer, boolean playSound) {
        Vec3 launchVelocity = new Vec3(0, 70, 0);
        serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer.getId(), launchVelocity));
        serverPlayer.setDeltaMovement(launchVelocity);
        if (playSound) {
            serverPlayer.playSound(SoundEvents.GENERIC_EXPLODE.value());
        }
    }

    private void teleportToLevel(ServerPlayer serverPlayer) {
        PartyData partyData = PartyData.get(serverPlayer.level());
        if (serverPlayer.level().dimension() == Level.OVERWORLD) {
            if (partyData.isInParty(serverPlayer.getUUID()) && !partyData.isLeader(serverPlayer.getUUID())) {
                PlayerDataUtils.setO2(serverPlayer, 19000);
                serverPlayer.teleportTo(serverPlayer.level().getServer().getPlayerList().getPlayer(partyData.getParty(serverPlayer.getUUID()).leader).level(), serverPlayer.getX(), 145, serverPlayer.getZ(), Set.of(), 0, 0, true);
                serverPlayer.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 120));
                loadPlanetInventory(serverPlayer, PlayerDataUtils.getPlanetContainer(serverPlayer));
            } else {
                ServerLevel world = InfiniverseAPI.get().getOrCreateLevel(serverPlayer.level().getServer(), ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath(MODID, serverPlayer.getStringUUID() + "planet" + serverPlayer.level().getRandom().nextInt())), () -> getWorldSettings(serverPlayer.level()));
                PlayerDataUtils.setSavedInventory(serverPlayer, new SaveableSimpleContainer(serverPlayer.getInventory()));
                loadPlanetInventory(serverPlayer, PlayerDataUtils.getPlanetContainer(serverPlayer));
                PlayerDataUtils.setO2(serverPlayer, 19000);
                serverPlayer.teleportTo(world, serverPlayer.getX(), 145, serverPlayer.getZ(), Set.of(), 0, 0, true);
                serverPlayer.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 140));
                createCanon(world, serverPlayer.blockPosition().getX(), serverPlayer.blockPosition().getZ(), serverPlayer);
            }
        } else {
            serverPlayer.teleportTo(serverPlayer.level().getServer().getLevel(Level.OVERWORLD), serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), Set.of(), 0, 0, true);
            PlayerDataUtils.setPlanetContainer(serverPlayer, new PlanetInventoryContainer(serverPlayer.getInventory()));
            loadOverworldInventory(serverPlayer, PlayerDataUtils.getSavedInventory(serverPlayer));
            serverPlayer.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 420));
            RoguePlanets.scheduleTask(419, () -> checkToAddSlowFalling(serverPlayer));
        }
    }

    public static void createCanon(ServerLevel level, int x, int z, ServerPlayer player) {
        int foundY = Integer.MIN_VALUE;
        for (int y = 150; y > level.getMinY(); y--) {
            BlockPos pos = new BlockPos(x, y, z);
            if (!level.getBlockState(pos).isAir()) {
                foundY = y;
                break;
            }
        }
        if (foundY == Integer.MIN_VALUE) return;

        BlockPos platformY = new BlockPos(x, foundY, z);
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                level.setBlock(platformY.offset(dx, 0, dz), Blocks.SMOOTH_STONE.defaultBlockState(), Block.UPDATE_ALL);
            }
        }
        CanonEntity canon = RPEntities.CANON.get().create(level, net.minecraft.world.entity.EntitySpawnReason.TRIGGERED);
        if (canon == null) return;
        canon.snapTo(x + 0.5, foundY + 1, z + 0.5, 0f, 0f);
        canon.setGlowingTag(true);
        level.addFreshEntity(canon);

        Inventory inventory = player.getInventory();
        inventory.getNonEquipmentItems().replaceAll(stack -> stack.is(Items.COMPASS) ? ItemStack.EMPTY : stack);
        if (inventory.getItem(Inventory.SLOT_OFFHAND).is(Items.COMPASS)) {
            inventory.setItem(Inventory.SLOT_OFFHAND, ItemStack.EMPTY);
        }

        ItemStack compass = new ItemStack(Items.COMPASS);
        compass.set(DataComponents.LODESTONE_TRACKER, new LodestoneTracker(Optional.of(GlobalPos.of(level.dimension(), platformY)), false));

        if (!player.getInventory().add(compass)) {
            player.drop(compass, false);
        }
    }

    public void checkToAddSlowFalling(ServerPlayer player) {
        BlockPos playerPos = player.blockPosition();
        BlockPos targetPos = playerPos.below(10);
        ServerLevel world = player.level();
        if (world.getBlockState(targetPos).isAir()) {
            player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 420));
        }
    }

    public LevelStem getWorldSettings(ServerLevel serverLevel) {
        MinecraftServer server = serverLevel.getServer();
        ServerLevel oldLevel = server.getLevel(RoguePlanets.MINER_DIMENSION);
        DynamicOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, server.registryAccess());
        ChunkGenerator oldChunkGenerator = oldLevel.getChunkSource().getGenerator();
        ChunkGenerator newChunkGenerator = ChunkGenerator.CODEC.encodeStart(ops, oldChunkGenerator)
                .flatMap(nbt -> ChunkGenerator.CODEC.parse(ops, nbt))
                .getOrThrow(s -> new IllegalStateException(String.format("Error copying dimension: %s", s)));
        Holder<DimensionType> typeHolder = oldLevel.dimensionTypeRegistration();
        // Each planet gets a fresh, genuinely random seed unrelated to the overworld's seed.
        long randomSeed = server.overworld().getRandom().nextLong();
        return new LevelStem(typeHolder, newChunkGenerator, OptionalLong.of(randomSeed));
    }

    private void loadPlanetInventory(ServerPlayer player, PlanetInventoryContainer cap) {
        for (int i = 0; i < cap.getItems().size(); i++) {
            ItemStack item = cap.getItems().get(i).copy();
            if (item.getItem() instanceof LevelableItem) {
                item.setDamageValue(-item.getMaxDamage());
            }
            player.getInventory().setItem(i, item);
        }
    }

    private void loadOverworldInventory(ServerPlayer player, SaveableSimpleContainer cap) {
        for (int i = 0; i < cap.getItems().size(); i++) {
            player.getInventory().setItem(i, cap.getItems().get(i).copy());
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return animatableInstanceCache;
    }
}
