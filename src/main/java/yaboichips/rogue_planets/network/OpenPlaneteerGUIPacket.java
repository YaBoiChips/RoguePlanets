package yaboichips.rogue_planets.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import yaboichips.rogue_planets.RoguePlanets;
import yaboichips.rogue_planets.client.ClientPacketHandler;

public record OpenPlaneteerGUIPacket(int credits) implements CustomPacketPayload {
    public static final Type<OpenPlaneteerGUIPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(RoguePlanets.MODID, "open_planeteer_gui"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenPlaneteerGUIPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, OpenPlaneteerGUIPacket::credits,
            OpenPlaneteerGUIPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(OpenPlaneteerGUIPacket packet, IPayloadContext context) {
        ClientPacketHandler.handle(packet);
    }
}
