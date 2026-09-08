package yaboichips.rogue_planets.common.entities.workers.augmentor;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import yaboichips.rogue_planets.network.ApplyAugmentPacket;
import yaboichips.rogue_planets.network.RoguePackets;
import yaboichips.rogue_planets.network.SendItemsToSlotPacket;

import static yaboichips.rogue_planets.RoguePlanets.MODID;

public class AugmentorScreen extends AbstractContainerScreen<AugmentorMenu> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(MODID, "textures/gui/augmentor.png");

    public AugmentorScreen(AugmentorMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 256, 256);
    }

    @Override
    protected void init() {
        super.init();
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        this.addRenderableWidget(Button.builder(Component.literal("Apply Augment"), button -> {
            RoguePackets.sendToServer(new ApplyAugmentPacket());
            this.minecraft.player.closeContainer();
        }).bounds(x + 82, y + 66, 90, 20).build());
    }

    @Override
    public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        guiGraphics.blit(TEXTURE, (this.width - this.imageWidth) / 2, (this.height - this.imageHeight) / 2, this.imageWidth, this.imageHeight, 0f, 0f, 1f, 1f);
    }

    @Override
    public void onClose() {
        RoguePackets.sendToServer(new SendItemsToSlotPacket());
        super.onClose();
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
        guiGraphics.text(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        guiGraphics.text(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
    }
}
