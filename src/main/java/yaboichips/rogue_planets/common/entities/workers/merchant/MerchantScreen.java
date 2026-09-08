package yaboichips.rogue_planets.common.entities.workers.merchant;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import yaboichips.rogue_planets.data.ClientPlayerData;
import yaboichips.rogue_planets.network.BuyItemPacket;
import yaboichips.rogue_planets.network.RoguePackets;
import yaboichips.rogue_planets.network.SellItemPacket;

import static yaboichips.rogue_planets.RoguePlanets.MODID;

public class MerchantScreen extends AbstractContainerScreen<MerchantMenu> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(MODID, "textures/gui/merchant.png");

    public MerchantScreen(MerchantMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 256, 256);
    }

    @Override
    protected void init() {
        super.init();
        this.clearWidgets();
        Button sellButton = Button.builder(Component.literal("Sell"), btn -> RoguePackets.sendToServer(new SellItemPacket())).pos(leftPos + 143, topPos + 76).size(50, 20).build();
        this.addRenderableWidget(sellButton);
        for (int i = 0; i < MerchantSales.SALES.size(); i++) {
            MerchantSales sale = MerchantSales.SALES.get(i);

            int x = this.leftPos + 6;
            int y = this.topPos + 6 + i * 20;

            Button button = new CustomItemButton(x, y, 76, 20, sale.item(), sale.price(), btn -> RoguePackets.sendToServer(new BuyItemPacket(sale.item(), sale.price())));

            this.addRenderableWidget(button);
        }
    }

    @Override
    public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.blit(TEXTURE, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, 0f, 0f, 1f, 1f);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(this.font, Component.literal(ClientPlayerData.getCredits() + "₡"), this.titleLabelX + 215, this.titleLabelY, 4210752, false);
    }

    private static class CustomItemButton extends Button {
        private final ItemStack itemStack;
        private final int price;

        public CustomItemButton(int x, int y, int width, int height, ItemStack itemStack, int price, OnPress onPress) {
            super(x, y, width, height, Component.empty(), onPress, DEFAULT_NARRATION);
            this.itemStack = itemStack;
            this.price = price;
        }

        @Override
        protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
            int background = this.isHoveredOrFocused() ? 0x80FFFFFF : 0x80000000;
            graphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), background);

            int iconX = this.getX() + 5;
            int iconY = this.getY() + (this.getHeight() - 16) / 2;
            graphics.item(itemStack, iconX, iconY);
            graphics.itemDecorations(Minecraft.getInstance().font, itemStack, iconX, iconY);

            int priceX = this.getX() + 45;
            int priceY = this.getY() + (this.getHeight() - 8) / 2;
            graphics.text(Minecraft.getInstance().font, price + "₡", priceX, priceY, 0xFFFFFF);
        }
    }
}
