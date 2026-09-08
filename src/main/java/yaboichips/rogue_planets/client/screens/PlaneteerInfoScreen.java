package yaboichips.rogue_planets.client.screens;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class PlaneteerInfoScreen extends Screen {
    private final int credits;
    private final List<IntRenderData> intDataList = new ArrayList<>();

    public PlaneteerInfoScreen(int credits) {
        super(Component.literal("Planeteer Manuel"));
        this.credits = credits;
    }

    @Override
    protected void init() {
        super.init();
        addInt("Credits", credits, 10, 10);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.extractBackground(guiGraphics, mouseX, mouseY, partialTicks);
        for (IntRenderData data : intDataList) {
            if (data.description == null) {
                guiGraphics.text(this.font, String.valueOf(data.value), data.x, data.y, 0xFFFFFF, false);
            } else {
                guiGraphics.text(this.font, data.description + ": " + data.value, data.x, data.y, 0xFFFFFF, false);
            }
        }
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
    }

    public void addInt(int value, int x, int y) {
        this.intDataList.add(new IntRenderData(null, value, x, y));
    }

    public void addInt(String description, int value, int x, int y) {
        this.intDataList.add(new IntRenderData(description, value, x, y));
    }

    private record IntRenderData(String description, int value, int x, int y) {
    }
}
