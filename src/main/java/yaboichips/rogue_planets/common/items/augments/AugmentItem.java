package yaboichips.rogue_planets.common.items.augments;

import net.minecraft.world.item.Item;

public class AugmentItem extends Item {
    private final AugmentType type;

    public AugmentItem(Properties properties, AugmentType type) {
        super(properties);
        this.type = type;
    }

    public AugmentType getType() {
        return type;
    }
}
