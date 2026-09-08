package yaboichips.rogue_planets.common.items;

import net.minecraft.world.item.ItemStack;
import yaboichips.rogue_planets.data.RPDataComponents;

public interface LevelableItem {
    default int getLevel(ItemStack stack) {
        return stack.getOrDefault(RPDataComponents.LEVEL.get(), 0);
    }

    default void setLevel(ItemStack stack, int i) {
        stack.set(RPDataComponents.LEVEL.get(), i);
    }

    default void levelUp(ItemStack stack) {
        if (getLevel(stack) < 20) {
            setLevel(stack, getLevel(stack) + 1);
        }
    }

    default int getLevelUpCost(ItemStack stack) {
        return stack.getOrDefault(RPDataComponents.LEVEL_COST.get(), 0);
    }

    default void setLevelUpCost(ItemStack stack, int i) {
        stack.set(RPDataComponents.LEVEL_COST.get(), i);
    }
}
