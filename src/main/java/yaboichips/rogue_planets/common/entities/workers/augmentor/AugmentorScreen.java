package yaboichips.rogue_planets.common.entities.workers.augmentor;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import yaboichips.rogue_planets.client.gui.RPButton;
import yaboichips.rogue_planets.client.gui.RPScreenUtil;
import yaboichips.rogue_planets.common.items.augments.AugmentTarget;
import yaboichips.rogue_planets.core.RPItems;
import yaboichips.rogue_planets.network.ApplyAugmentPacket;
import yaboichips.rogue_planets.network.RoguePackets;
import yaboichips.rogue_planets.network.SelectAugmentTargetPacket;

public class AugmentorScreen extends AbstractContainerScreen<AugmentorMenu> {

    public AugmentorScreen(AugmentorMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 256;
        this.imageHeight = 256;
    }

    @Override
    protected void init() {
        super.init();
        int x = this.leftPos;
        int y = this.topPos;

        AugmentTarget[] targets = AugmentTarget.values();
        for (int i = 0; i < targets.length; i++) {
            AugmentTarget target = targets[i];
            this.addRenderableWidget(RPButton.iconWithLabel(
                    x + 8, y + 8 + i * 22, 90, 20,
                    () -> iconFor(target),
                    Component.literal(target.getLabel()),
                    Component.literal("Select your " + target.getLabel()),
                    btn -> RoguePackets.CHANNEL.sendToServer(new SelectAugmentTargetPacket(target))
            ));
        }

        this.addRenderableWidget(RPButton.text(x + 128, y + 90, 110, 20, Component.literal("Apply Augment"), btn ->
                RoguePackets.CHANNEL.sendToServer(new ApplyAugmentPacket())
        ).withTooltip(Component.literal("Installs the augment crystal onto the selected item")));
    }

    private static ItemStack iconFor(AugmentTarget target) {
        return switch (target) {
            case HELMET -> RPItems.PLANETEER_HELMET.get().getDefaultInstance();
            case CHESTPLATE -> RPItems.PLANETEER_CHESTPLATE.get().getDefaultInstance();
            case LEGGINGS -> RPItems.PLANETEER_LEGGINGS.get().getDefaultInstance();
            case BOOTS -> RPItems.PLANETEER_BOOTS.get().getDefaultInstance();
            case PICKAXE -> RPItems.PLANETEER_PICKAXE.get().getDefaultInstance();
        };
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RPScreenUtil.renderPanel(guiGraphics, this.leftPos, this.topPos, this.imageWidth, this.imageHeight);
        RPScreenUtil.renderSlots(guiGraphics, this.menu.slots, this.leftPos, this.topPos);
        guiGraphics.drawString(this.font, "Item", this.leftPos + 148, this.topPos + 14, 0xFFFFFF, true);
        guiGraphics.drawString(this.font, "Augment", this.leftPos + 148, this.topPos + 48, 0xFFFFFF, true);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // No title / "Inventory" text in this GUI - the panel speaks for itself.
    }

    @Override
    public void render(GuiGraphics poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }
}
