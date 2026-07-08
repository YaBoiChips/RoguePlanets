package yaboichips.rogue_planets.mixin;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
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

    @Inject(method = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;hasClickedOutside(DDIII)Z", at = @At("HEAD"), cancellable = true)
    private void blockDropKey(double p_98845_, double p_98846_, int p_98847_, int p_98848_, int p_98849_, CallbackInfoReturnable<Boolean> cir) {
        if (menu instanceof MerchantMenu || menu instanceof ForgeMasterMenu || menu instanceof CEOMenu || menu instanceof AugmentorMenu) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;keyPressed(III)Z", at = @At("HEAD"), cancellable = true)
    private void blockDropKey(int keyCode, int p_97766_, int p_97767_, CallbackInfoReturnable<Boolean> cir) {
        if (keyCode == GLFW.GLFW_KEY_Q) {
            if (menu instanceof MerchantMenu || menu instanceof ForgeMasterMenu || menu instanceof CEOMenu || menu instanceof AugmentorMenu) {
                cir.setReturnValue(true);
            }
        }
    }
}
