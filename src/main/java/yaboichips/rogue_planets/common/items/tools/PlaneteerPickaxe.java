package yaboichips.rogue_planets.common.items.tools;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import yaboichips.rogue_planets.common.items.LevelableItem;
import yaboichips.rogue_planets.data.AugmentData;
import yaboichips.rogue_planets.data.RPDataComponents;

import java.util.function.Consumer;

public class PlaneteerPickaxe extends Item implements LevelableItem {
    public PlaneteerPickaxe(Properties properties) {
        super(properties.pickaxe(ToolMaterial.IRON, 1, -2.8F).component(RPDataComponents.AUGMENT_DATA.get(), AugmentData.EMPTY));
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
            return 0.0F;
        }
        return Math.round(super.getDestroySpeed(stack, state) * Math.pow(1 + 0.1, getLevel(stack)));
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return stack.getDamageValue() < stack.getMaxDamage() - 1 && super.isCorrectToolForDrops(stack, state);
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
            return;
        }
        super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> components, TooltipFlag flag) {
        components.accept(Component.literal("Level " + getLevel(stack)));
        super.appendHoverText(stack, context, tooltipDisplay, components, flag);
    }

    public void levelUp(ItemStack stack) {
        setLevel(stack, getLevel(stack) + 1);
        int newMax = (int) Math.round(50 * Math.pow(1.1, getLevel(stack)));
        stack.set(DataComponents.MAX_DAMAGE, newMax);
        if (getLevel(stack) == 5 || getLevel(stack) == 10 || getLevel(stack) == 15) {
            int slots = getLevel(stack) == 5 ? 1 : getLevel(stack) == 10 ? 2 : 3;
            AugmentData current = stack.getOrDefault(RPDataComponents.AUGMENT_DATA.get(), AugmentData.EMPTY);
            stack.set(RPDataComponents.AUGMENT_DATA.get(), new AugmentData(slots, current.augments()));
        }
    }
}
