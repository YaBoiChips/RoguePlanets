package yaboichips.rogue_planets.common.items.tools;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolAction;
import org.jetbrains.annotations.Nullable;
import yaboichips.rogue_planets.common.items.LevelableItem;
import yaboichips.rogue_planets.common.items.augments.AugmentType;
import yaboichips.rogue_planets.common.items.augments.AugmentUtil;

import java.util.List;

public class PlaneteerPickaxe extends PickaxeItem implements LevelableItem {
    private static final float WALL_JUMP_HORIZONTAL = 1.2F;
    private static final float WALL_JUMP_VERTICAL = 0.7F;
    private static final int WALL_JUMP_COOLDOWN_TICKS = 30;

    public PlaneteerPickaxe() {
        super(Tiers.IRON, 1, -2.8F, new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (AugmentUtil.getAugment(stack) == AugmentType.WALL_JUMP) {
            if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer && !player.getCooldowns().isOnCooldown(this)) {
                Vec3 look = player.getLookAngle();
                player.setDeltaMovement(player.getDeltaMovement().add(look.x * WALL_JUMP_HORIZONTAL, WALL_JUMP_VERTICAL, look.z * WALL_JUMP_HORIZONTAL));
                serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(player));
                player.getCooldowns().addCooldown(this, WALL_JUMP_COOLDOWN_TICKS);
            }
            return InteractionResultHolder.success(stack);
        }
        if (player.getMainHandItem().getDamageValue()< 1) {
            return InteractionResultHolder.fail(player.getMainHandItem());
        }
        return super.use(level, player, hand);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
            return 0.0F;
        }
        float speed = (float) Math.round(super.getDestroySpeed(stack, state) * Math.pow(1 + 0.1, getLevel(stack)));
        if (AugmentUtil.getAugment(stack) == AugmentType.BERSERK_MINING) {
            float wornFraction = stack.getDamageValue() / (float) stack.getMaxDamage();
            speed *= 1.0F + wornFraction;
        }
        return speed;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return (int) Math.round((50 * Math.pow(1 + 0.1, getLevel(stack))));
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return stack.getDamageValue() < stack.getMaxDamage() - 1 && super.isCorrectToolForDrops(stack, state);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
            return false;
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_41422_, List<Component> components, TooltipFlag p_41424_) {
        if (stack.getItem() instanceof LevelableItem) {
            components.add(Component.literal("Level " + stack.getTag().getInt("Level")));
        }
        AugmentType augment = AugmentUtil.getAugment(stack);
        components.add(Component.literal(augment == null ? "No augment installed" : "Augment: " + augment.getDisplayName()));
        super.appendHoverText(stack, p_41422_, components, p_41424_);
    }

    public void levelUp(ItemStack stack) {
        setLevel(stack, getLevel(stack) + 1);
        CompoundTag tag = stack.getTag();
        tag.putInt("Level", getLevel(stack));
        stack.setTag(tag);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        if (stack.getDamageValue() <= 1){
            return false;
        }
        return super.canPerformAction(stack, toolAction);
    }
}
