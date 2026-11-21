package yaboichips.rogue_planets.common.items.augments;

import net.minecraft.world.entity.EquipmentSlot;
import org.jetbrains.annotations.Nullable;

/**
 * The item slot an augment can be installed into. Ordinal 0-3 matches the slot
 * index used by {@link yaboichips.rogue_planets.common.containers.PlanetArmorContainer}.
 */
public enum AugmentTarget {
    HELMET(EquipmentSlot.HEAD, "Helmet"),
    CHESTPLATE(EquipmentSlot.CHEST, "Jacket"),
    LEGGINGS(EquipmentSlot.LEGS, "Pantaloons"),
    BOOTS(EquipmentSlot.FEET, "Boots"),
    PICKAXE(null, "Pickaxe");

    private final EquipmentSlot equipmentSlot;
    private final String label;

    AugmentTarget(@Nullable EquipmentSlot equipmentSlot, String label) {
        this.equipmentSlot = equipmentSlot;
        this.label = label;
    }

    @Nullable
    public EquipmentSlot getEquipmentSlot() {
        return equipmentSlot;
    }

    public String getLabel() {
        return label;
    }

    public int getArmorContainerSlot() {
        return ordinal();
    }
}
