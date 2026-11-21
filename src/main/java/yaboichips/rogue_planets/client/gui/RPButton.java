package yaboichips.rogue_planets.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Every button in every Rogue Planets GUI goes through here: a flat, fully code-drawn face
 * (no vanilla widgets texture) that can show an item icon, a text label, or both. Pass an
 * icon wherever the button maps onto a concrete item so the player sees it, with hover text
 * explaining what pressing it does.
 */
public class RPButton extends Button {
    @Nullable
    private final Supplier<ItemStack> icon;

    private RPButton(int x, int y, int width, int height, Component message, @Nullable Supplier<ItemStack> icon, OnPress onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.icon = icon;
    }

    public static RPButton text(int x, int y, int width, int height, Component label, OnPress onPress) {
        return new RPButton(x, y, width, height, label, null, onPress);
    }

    public static RPButton icon(int x, int y, int size, Supplier<ItemStack> icon, Component tooltip, OnPress onPress) {
        RPButton button = new RPButton(x, y, size, size, Component.empty(), icon, onPress);
        button.setTooltip(Tooltip.create(tooltip));
        return button;
    }

    public static RPButton iconWithLabel(int x, int y, int width, int height, Supplier<ItemStack> icon, Component label, Component tooltip, OnPress onPress) {
        RPButton button = new RPButton(x, y, width, height, label, icon, onPress);
        button.setTooltip(Tooltip.create(tooltip));
        return button;
    }

    public RPButton withTooltip(Component tooltip) {
        this.setTooltip(Tooltip.create(tooltip));
        return this;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        boolean hovered = isHoveredOrFocused();
        int x = getX();
        int y = getY();

        guiGraphics.fill(x, y, x + width, y + height, 0xFF1A1A1A);
        guiGraphics.fill(x + 1, y + 1, x + width - 1, y + height - 1, hovered ? 0xFF6B6B6B : 0xFF4A4A4A);
        guiGraphics.fill(x + 2, y + 2, x + width - 2, y + height - 2, hovered ? 0xFF4A4A4A : 0xFF323232);
        if (!active) {
            guiGraphics.fill(x + 2, y + 2, x + width - 2, y + height - 2, 0x90000000);
        }

        ItemStack stack = icon == null ? ItemStack.EMPTY : icon.get();
        boolean hasIcon = stack != null && !stack.isEmpty();
        boolean hasText = !getMessage().getString().isEmpty();

        if (hasIcon && hasText) {
            int iconX = x + 3;
            int iconY = y + (height - 16) / 2;
            guiGraphics.renderItem(stack, iconX, iconY);
            guiGraphics.renderItemDecorations(Minecraft.getInstance().font, stack, iconX, iconY);
            int textX = iconX + 20;
            int textY = y + (height - 8) / 2;
            guiGraphics.drawString(Minecraft.getInstance().font, getMessage(), textX, textY, active ? 0xFFFFFF : 0xA0A0A0, false);
        } else if (hasIcon) {
            int iconX = x + (width - 16) / 2;
            int iconY = y + (height - 16) / 2;
            guiGraphics.renderItem(stack, iconX, iconY);
            guiGraphics.renderItemDecorations(Minecraft.getInstance().font, stack, iconX, iconY);
        } else if (hasText) {
            int textColor = active ? 0xFFFFFF : 0xA0A0A0;
            guiGraphics.drawCenteredString(Minecraft.getInstance().font, getMessage(), x + width / 2, y + (height - 8) / 2, textColor);
        }
    }
}
