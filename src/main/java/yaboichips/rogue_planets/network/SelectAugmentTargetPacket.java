package yaboichips.rogue_planets.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import yaboichips.rogue_planets.common.entities.workers.augmentor.AugmentorMenu;
import yaboichips.rogue_planets.common.items.augments.AugmentTarget;

import java.util.function.Supplier;

public class SelectAugmentTargetPacket {
    private final AugmentTarget target;

    public SelectAugmentTargetPacket(AugmentTarget target) {
        this.target = target;
    }

    public static void encode(SelectAugmentTargetPacket packet, FriendlyByteBuf buf) {
        buf.writeEnum(packet.target);
    }

    public static SelectAugmentTargetPacket decode(FriendlyByteBuf buf) {
        return new SelectAugmentTargetPacket(buf.readEnum(AugmentTarget.class));
    }

    public static void handle(SelectAugmentTargetPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                if (context.getSender().containerMenu instanceof AugmentorMenu menu) {
                    menu.selectTarget(packet.target);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
