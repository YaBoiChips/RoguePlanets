package yaboichips.rogue_planets.common.entities.workers.merchant;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import yaboichips.rogue_planets.capabilties.player.ClientPlayerData;
import yaboichips.rogue_planets.client.gui.RPButton;
import yaboichips.rogue_planets.client.gui.RPScreenUtil;
import yaboichips.rogue_planets.network.BuyItemPacket;
import yaboichips.rogue_planets.network.RoguePackets;
import yaboichips.rogue_planets.network.SellItemPacket;

public class MerchantScreen extends AbstractContainerScreen<MerchantMenu> {

    public MerchantScreen(MerchantMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 256;
        this.imageHeight = 256;
    }

    @Override
    protected void init() {
        super.init();
        this.clearWidgets();

        this.addRenderableWidget(RPButton.text(leftPos + 143, topPos + 76, 50, 20, Component.literal("Sell"), btn ->
                RoguePackets.CHANNEL.sendToServer(new SellItemPacket())
        ).withTooltip(Component.literal("Sells whatever is in the sell slot")));

        for (int i = 0; i < MerchantSales.SALES.size(); i++) {
            MerchantSales sale = MerchantSales.SALES.get(i);
            int x = this.leftPos + 6;
            int y = this.topPos + 6 + i * 20;

            this.addRenderableWidget(RPButton.iconWithLabel(
                    x, y, 76, 20,
                    sale.item()::copy,
                    Component.literal(sale.price() + "₡"),
                    sale.item().getHoverName(),
                    btn -> RoguePackets.CHANNEL.sendToServer(new BuyItemPacket(sale.item(), sale.price()))
            ));
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RPScreenUtil.renderPanel(guiGraphics, this.leftPos, this.topPos, this.imageWidth, this.imageHeight);
        RPScreenUtil.renderSlots(guiGraphics, this.menu.slots, this.leftPos, this.topPos);
    }

    @Override
    public void render(GuiGraphics poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, Component.literal(ClientPlayerData.getCredits() + "₡"), this.titleLabelX + 215, this.titleLabelY, 4210752, false);
    }
}
