package yaboichips.rogue_planets.common.entities.workers.forgemaster;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import yaboichips.rogue_planets.capabilties.player.ClientPlayerData;
import yaboichips.rogue_planets.client.gui.RPButton;
import yaboichips.rogue_planets.client.gui.RPScreenUtil;
import yaboichips.rogue_planets.network.LevelUpItemPacket;
import yaboichips.rogue_planets.network.RoguePackets;

public class ForgeMasterScreen extends AbstractContainerScreen<ForgeMasterMenu> {

    public ForgeMasterScreen(ForgeMasterMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 256;
        this.imageHeight = 256;
    }

    @Override
    protected void init() {
        super.init();
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        this.addRenderableWidget(RPButton.text(x + 96, y + 70, 60, 20, Component.literal("Level Up"), button ->
                RoguePackets.CHANNEL.sendToServer(new LevelUpItemPacket())
        ).withTooltip(Component.literal("Cost to upgrade, Level * 50₡")));
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RPScreenUtil.renderPanel(guiGraphics, this.leftPos, this.topPos, this.imageWidth, this.imageHeight);
        RPScreenUtil.renderSlots(guiGraphics, this.menu.slots, this.leftPos, this.topPos);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, Component.literal(ClientPlayerData.getCredits() + "₡"), this.titleLabelX + 215, this.titleLabelY, 4210752, false);
    }

    @Override
    public void render(GuiGraphics poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }
}
