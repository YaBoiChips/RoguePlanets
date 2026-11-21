package yaboichips.rogue_planets.mixin;

import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yaboichips.rogue_planets.common.world.DimensionSeeds;

/**
 * ServerLevel#getSeed() is what actually feeds chunk/structure generation (ChunkMap reads it
 * when building its RandomState), and it otherwise always returns the server's single global
 * world seed for every dimension. This lets specific dimensions - the ones Infiniverse creates
 * for Rogue Planets - generate from a different seed instead, without ever touching the real
 * world seed other dimensions (overworld included) still read.
 */
@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {

    @Inject(method = "getSeed", at = @At("RETURN"), cancellable = true)
    private void rogueplanets$useDimensionSeed(CallbackInfoReturnable<Long> cir) {
        ServerLevel self = (ServerLevel) (Object) this;
        if (DimensionSeeds.has(self.dimension())) {
            cir.setReturnValue(DimensionSeeds.get(self.dimension(), cir.getReturnValue()));
        }
    }
}
