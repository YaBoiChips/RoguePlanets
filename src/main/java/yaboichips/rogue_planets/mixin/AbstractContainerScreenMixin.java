package yaboichips.rogue_planets.mixin;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yaboichips.rogue_planets.common.entities.workers.augmentor.AugmentorMenu;
import yaboichips.rogue_planets.common.entities.workers.ceo.CEOMenu;
import yaboichips.rogue_planets.common.entities.workers.forgemaster.ForgeMasterMenu;
import yaboichips.rogue_planets.common.entities.workers.merchant.MerchantMenu;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {

    @Shadow
    protected AbstractContainerMenu menu;

    @Inject(method = "hasClickedOutside", at = @At("HEAD"), cancellable = true)
    private void blockClickOutside(double mouseX, double mouseY, int guiLeft, int guiTop, CallbackInfoReturnable<Boolean> cir) {
        if (menu instanceof MerchantMenu || menu instanceof ForgeMasterMenu || menu instanceof CEOMenu || menu instanceof AugmentorMenu) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void blockDropKey(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
        if (event.key() == GLFW.GLFW_KEY_Q) {
            if (menu instanceof MerchantMenu || menu instanceof ForgeMasterMenu || menu instanceof CEOMenu || menu instanceof AugmentorMenu) {
                cir.setReturnValue(true);
            }
        }
    }
}
