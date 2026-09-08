package yaboichips.rogue_planets.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import yaboichips.rogue_planets.RoguePlanets;
import yaboichips.rogue_planets.data.ClientPlayerData;

public record SendPlayerDataPacket(int o2, int credits) implements CustomPacketPayload {
    public static final Type<SendPlayerDataPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(RoguePlanets.MODID, "send_player_data"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SendPlayerDataPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SendPlayerDataPacket::o2,
            ByteBufCodecs.VAR_INT, SendPlayerDataPacket::credits,
            SendPlayerDataPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SendPlayerDataPacket packet, IPayloadContext context) {
        ClientPlayerData.setO2(packet.o2());
        ClientPlayerData.setCredits(packet.credits());
    }
}
