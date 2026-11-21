package yaboichips.rogue_planets.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;

import java.util.List;

import static yaboichips.rogue_planets.RoguePlanets.MODID;

/**
 * Shared chrome for every Rogue Planets container screen: one background image, everything
 * else (panel tint, slot squares) drawn in code instead of baked into a per-GUI texture.
 */
public class RPScreenUtil {
    public static final ResourceLocation BACKGROUND = new ResourceLocation(MODID, "textures/gui/background.png");
    private static final int BACKGROUND_SIZE = 640;
    private static final float BACKGROUND_ALPHA = 1;
    private static final int PANEL_TINT = 0x90000000;
    private static final int PANEL_BORDER = 0xFF8B8B8B;

    private static final int SLOT_BORDER = 0xFF8B8B8B;
    private static final int SLOT_WELL = 0xFF2B2B2B;

    private RPScreenUtil() {
    }

    public static void renderPanel(GuiGraphics guiGraphics, int x, int y, int width, int height) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1F, 1F, 1F, BACKGROUND_ALPHA);
        guiGraphics.blit(BACKGROUND, x, y, width, height, 0, 0, BACKGROUND_SIZE, BACKGROUND_SIZE, BACKGROUND_SIZE, BACKGROUND_SIZE);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

//        guiGraphics.fill(x, y, x + width, y + height, PANEL_TINT);
        guiGraphics.fill(x, y, x + width, y + 1, PANEL_BORDER);
        guiGraphics.fill(x, y + height - 1, x + width, y + height, PANEL_BORDER);
        guiGraphics.fill(x, y, x + 1, y + height, PANEL_BORDER);
        guiGraphics.fill(x + width - 1, y, x + width, y + height, PANEL_BORDER);
        RenderSystem.disableBlend();
    }

    public static void renderSlot(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.fill(x - 1, y - 1, x + 17, y + 17, SLOT_BORDER);
        guiGraphics.fill(x, y, x + 16, y + 16, SLOT_WELL);
    }

    public static void renderSlots(GuiGraphics guiGraphics, List<Slot> slots, int leftPos, int topPos) {
        for (Slot slot : slots) {
            renderSlot(guiGraphics, leftPos + slot.x, topPos + slot.y);
        }
    }
}
