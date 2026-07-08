package yaboichips.rogue_planets.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import yaboichips.rogue_planets.common.entities.workers.augmentor.AugmentorMenu;

import java.util.function.Supplier;

public class ApplyAugmentPacket {
    public ApplyAugmentPacket() {
    }


    public void encode(FriendlyByteBuf buf) {

    }


    public static ApplyAugmentPacket decode(FriendlyByteBuf buf) {
        return new ApplyAugmentPacket();
    }


    public static void handle(ApplyAugmentPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                // Get the player and their open menu
                if (context.getSender().containerMenu instanceof AugmentorMenu menu) {
                    menu.applyAugment();
                }
            }
        });
        context.setPacketHandled(true);
    }
}
