package yaboichips.rogue_planets.events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import yaboichips.rogue_planets.RoguePlanets;
import yaboichips.rogue_planets.common.items.augments.AugmentType;
import yaboichips.rogue_planets.common.items.augments.AugmentUtil;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Central home for every augment's gameplay effect. Armor/pickaxe effects are driven off whatever
 * is actually worn/held (vanilla equipment slots), not off the Augmentor's storage containers -
 * the Augmentor is just where you install them.
 */
@Mod.EventBusSubscriber(modid = RoguePlanets.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AugmentEvents {
    private static final int MAX_VEIN_BLOCKS = 32;
    private static final int AUTO_FEED_CHECK_INTERVAL = 20;
    private static final int AUTO_FEED_HUNGER_THRESHOLD = 16; // 20 max, "down 2 bars" = 4 points

    private static final Map<UUID, Integer> autoFeedTimers = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.side.isClient()) return;
        if (!(event.player instanceof ServerPlayer player)) return;
        if (!player.isAlive()) return;

        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);

        player.setMaxUpStep(AugmentUtil.getAugment(boots) == AugmentType.STEP_UP ? 1.0F : 0.6F);

        if (AugmentUtil.getAugment(boots) == AugmentType.LOWER_GRAVITY && player.getDeltaMovement().y < -0.08) {
            player.setDeltaMovement(player.getDeltaMovement().multiply(1, 0.6, 1));
        }

        if (AugmentUtil.getAugment(helmet) == AugmentType.NIGHT_VISION) {
            giveHiddenEffect(player, MobEffects.NIGHT_VISION, 220);
        }

        if (AugmentUtil.getAugment(legs) == AugmentType.SPEED) {
            giveHiddenEffect(player, MobEffects.MOVEMENT_SPEED, 220);
        }

        if (AugmentUtil.getAugment(legs) == AugmentType.AUTO_FEED) {
            handleAutoFeed(player);
        }
    }

    private static void giveHiddenEffect(ServerPlayer player, MobEffect effect, int minDuration) {
        MobEffectInstance current = player.getEffect(effect);
        if (current == null || current.getDuration() < minDuration - 20) {
            player.addEffect(new MobEffectInstance(effect, minDuration, 0, true, false, false));
        }
    }

    private static void handleAutoFeed(ServerPlayer player) {
        int ticks = autoFeedTimers.merge(player.getUUID(), 1, Integer::sum);
        if (ticks < AUTO_FEED_CHECK_INTERVAL) return;
        autoFeedTimers.put(player.getUUID(), 0);

        if (player.getFoodData().getFoodLevel() > AUTO_FEED_HUNGER_THRESHOLD) return;
        if (player.getRandom().nextInt(3) != 0) return;

        for (ItemStack stack : player.getInventory().items) {
            if (!stack.getItem().isEdible()) continue;
            FoodProperties food = stack.getFoodProperties(player);
            if (food == null) continue;
            player.getFoodData().eat(food.getNutrition(), food.getSaturationModifier());
            stack.shrink(1);
            break;
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        autoFeedTimers.remove(event.getEntity().getUUID());
    }

    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
        if (AugmentUtil.getAugment(boots) == AugmentType.LOWER_GRAVITY) {
            event.setDamageMultiplier(event.getDamageMultiplier() * 0.3F);
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        if (AugmentUtil.getAugment(chest) != AugmentType.THORNS) return;
        if (event.getSource().getEntity() instanceof LivingEntity attacker && attacker != player) {
            attacker.hurt(player.damageSources().thorns(player), 2.0F);
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        ItemStack tool = player.getMainHandItem();
        if (AugmentUtil.getAugment(tool) != AugmentType.VEIN_MINING) return;

        BlockState originalState = event.getState();
        if (!isVeinable(originalState)) return;

        Block block = originalState.getBlock();
        BlockPos origin = event.getPos();
        Set<BlockPos> vein = floodFill(level, origin, block, MAX_VEIN_BLOCKS);
        vein.remove(origin);

        for (BlockPos pos : vein) {
            if (level.getBlockState(pos).getBlock() != block) continue;
            level.destroyBlock(pos, true, player);
            if (!tool.isEmpty() && tool.isDamageableItem()) {
                tool.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                if (tool.isEmpty()) break;
            }
        }
    }

    private static boolean isVeinable(BlockState state) {
        if (state.is(Tags.Blocks.ORES)) return true;
        ResourceLocation id = ForgeRegistries.BLOCKS.getKey(state.getBlock());
        return id != null && id.getPath().contains("ore");
    }

    private static Set<BlockPos> floodFill(ServerLevel level, BlockPos origin, Block block, int max) {
        Set<BlockPos> visited = new HashSet<>();
        Deque<BlockPos> queue = new ArrayDeque<>();
        visited.add(origin);
        queue.add(origin);

        while (!queue.isEmpty() && visited.size() < max) {
            BlockPos current = queue.poll();
            for (Direction direction : Direction.values()) {
                BlockPos next = current.relative(direction);
                if (visited.contains(next)) continue;
                if (level.getBlockState(next).getBlock() == block) {
                    visited.add(next);
                    queue.add(next);
                    if (visited.size() >= max) break;
                }
            }
        }
        return visited;
    }
}
