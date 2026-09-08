package yaboichips.rogue_planets.common.entities.workers.forgemaster;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import yaboichips.rogue_planets.data.ClientPlayerData;
import yaboichips.rogue_planets.network.LevelUpItemPacket;
import yaboichips.rogue_planets.network.RoguePackets;

import static yaboichips.rogue_planets.RoguePlanets.MODID;

public class ForgeMasterScreen extends AbstractContainerScreen<ForgeMasterMenu> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(MODID, "textures/gui/forge_master.png");
    private final ForgeMasterMenu menu;

    public ForgeMasterScreen(ForgeMasterMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 256, 256);
        this.menu = menu;
    }

    @Override
    protected void init() {
        super.init();
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        this.addRenderableWidget(Button.builder(Component.literal("Level Up"), button -> {
            RoguePackets.sendToServer(new LevelUpItemPacket());
        }).tooltip(Tooltip.create(Component.literal("Cost to upgrade, Level * 50₡"))).bounds(x + 96, y + 70, 60, 20).build());
    }

    @Override
    public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        guiGraphics.blit(TEXTURE, (this.width - this.imageWidth) / 2, (this.height - this.imageHeight) / 2, this.imageWidth, this.imageHeight, 0f, 0f, 1f, 1f);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
        guiGraphics.text(this.font, Component.literal(ClientPlayerData.getCredits() + "₡"), this.titleLabelX + 215, this.titleLabelY, 4210752, false);
    }
}
