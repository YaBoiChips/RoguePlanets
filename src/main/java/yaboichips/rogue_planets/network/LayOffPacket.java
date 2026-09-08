package yaboichips.rogue_planets.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import yaboichips.rogue_planets.RoguePlanets;
import yaboichips.rogue_planets.common.entities.workers.ceo.CEOMenu;

public record LayOffPacket() implements CustomPacketPayload {
    public static final Type<LayOffPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(RoguePlanets.MODID, "lay_off"));
    public static final StreamCodec<RegistryFriendlyByteBuf, LayOffPacket> STREAM_CODEC = StreamCodec.unit(new LayOffPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(LayOffPacket packet, IPayloadContext context) {
        if (context.player() instanceof ServerPlayer serverPlayer && serverPlayer.containerMenu instanceof CEOMenu menu) {
            menu.layOff();
        }
    }
}
