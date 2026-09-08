package yaboichips.rogue_planets.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import yaboichips.rogue_planets.RoguePlanets;
import yaboichips.rogue_planets.common.entities.workers.augmentor.AugmentorMenu;

public record SendItemsToSlotPacket() implements CustomPacketPayload {
    public static final Type<SendItemsToSlotPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(RoguePlanets.MODID, "send_items_to_slot"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SendItemsToSlotPacket> STREAM_CODEC = StreamCodec.unit(new SendItemsToSlotPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SendItemsToSlotPacket packet, IPayloadContext context) {
        if (context.player() instanceof ServerPlayer serverPlayer && serverPlayer.containerMenu instanceof AugmentorMenu menu) {
            menu.onClose();
        }
    }
}
