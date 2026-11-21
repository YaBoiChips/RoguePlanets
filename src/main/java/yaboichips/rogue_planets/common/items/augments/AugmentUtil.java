package yaboichips.rogue_planets.common.items.augments;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import yaboichips.rogue_planets.common.containers.PlanetArmorContainer;
import yaboichips.rogue_planets.common.containers.PlanetInventoryContainer;
import yaboichips.rogue_planets.core.RPItems;

/**
 * Augments live directly in the item's NBT ("Augment" -> {@link AugmentType} name) instead of a
 * capability, so there's exactly one source of truth and no syncing to worry about.
 */
public class AugmentUtil {
    private static final String TAG = "Augment";

    private AugmentUtil() {
    }

    public static boolean hasAugment(ItemStack stack) {
        return !stack.isEmpty() && stack.hasTag() && stack.getTag().contains(TAG);
    }

    @Nullable
    public static AugmentType getAugment(ItemStack stack) {
        if (!hasAugment(stack)) return null;
        try {
            return AugmentType.valueOf(stack.getTag().getString(TAG));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static void setAugment(ItemStack stack, AugmentType type) {
        stack.getOrCreateTag().putString(TAG, type.name());
    }

    public static void clearAugment(ItemStack stack) {
        if (stack.hasTag()) {
            stack.getTag().remove(TAG);
        }
    }

    /**
     * Resolves the live item for a given target: the pickaxe is searched for in the planet
     * inventory, armor pieces are read straight out of their fixed armor-container slot. The
     * returned stack is the actual reference stored in the container, not a copy.
     */
    public static ItemStack getTargetItem(AugmentTarget target, PlanetArmorContainer armor, PlanetInventoryContainer inventory) {
        if (target == AugmentTarget.PICKAXE) {
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                ItemStack stack = inventory.getItem(i);
                if (stack.is(RPItems.PLANETEER_PICKAXE.get())) {
                    return stack;
                }
            }
            return ItemStack.EMPTY;
        }
        return armor.getItem(target.getArmorContainerSlot());
    }
}
