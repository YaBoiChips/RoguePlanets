package yaboichips.rogue_planets.common.items.augments;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * The physical crystal/chip representing a single {@link AugmentType}. Consumed at the
 * Augmentor when installed onto an augmentable item.
 */
public class AugmentItem extends Item {
    private final AugmentType type;

    public AugmentItem(AugmentType type) {
        super(new Properties());
        this.type = type;
    }

    public AugmentType getType() {
        return type;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        components.add(Component.literal(type.getDescription()).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, level, components, flag);
    }
}
