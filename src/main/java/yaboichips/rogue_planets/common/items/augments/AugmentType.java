package yaboichips.rogue_planets.common.items.augments;

import java.util.EnumSet;
import java.util.Set;

/**
 * Every augment that can be installed on an {@link yaboichips.rogue_planets.common.items.ExplorerSuit}
 * piece or the {@link yaboichips.rogue_planets.common.items.tools.PlaneteerPickaxe} at the Augmentor.
 * Only one augment can live on a given item at a time (see {@link AugmentUtil}).
 */
public enum AugmentType {
    LOWER_GRAVITY("Lower Gravity", "Fall slower and take less fall damage", AugmentTarget.BOOTS),
    STEP_UP("Player Step Up", "Step up full blocks without jumping", AugmentTarget.BOOTS),
    DOUBLE_JUMP("Double Jump", "Jump again after being airborne for over a second", AugmentTarget.BOOTS),

    EXTRA_TANK("Extra Tank", "Extends how long your O2 supply lasts", AugmentTarget.HELMET, AugmentTarget.CHESTPLATE),
    NIGHT_VISION("Night Vision", "Grants night vision while worn", AugmentTarget.HELMET),

    POCKETS("Pockets", "Holds extra items, open with the pockets key", AugmentTarget.CHESTPLATE, AugmentTarget.LEGGINGS),
    THORNS("Thorns", "Reflects some damage back at attackers", AugmentTarget.CHESTPLATE),

    SPEED("Speed", "Increases movement speed while worn", AugmentTarget.LEGGINGS),
    AUTO_FEED("Auto Feed", "Automatically eats food from your inventory when hungry", AugmentTarget.LEGGINGS),

    BERSERK_MINING("Berserk Mining", "Mines faster the more worn down the pickaxe is", AugmentTarget.PICKAXE),
    WALL_JUMP("Wall Jump", "Right click to launch yourself forward and up", AugmentTarget.PICKAXE),
    VEIN_MINING("Vein Mining", "Breaks the whole connected ore vein at once", AugmentTarget.PICKAXE);

    private final String displayName;
    private final String description;
    private final Set<AugmentTarget> validTargets;

    AugmentType(String displayName, String description, AugmentTarget... validTargets) {
        this.displayName = displayName;
        this.description = description;
        this.validTargets = EnumSet.copyOf(java.util.Arrays.asList(validTargets));
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public Set<AugmentTarget> getValidTargets() {
        return validTargets;
    }

    public boolean isValidFor(AugmentTarget target) {
        return validTargets.contains(target);
    }
}
