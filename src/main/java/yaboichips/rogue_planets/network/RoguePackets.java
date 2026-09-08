package yaboichips.rogue_planets.network;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import static yaboichips.rogue_planets.RoguePlanets.MODID;

public class RoguePackets {

    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(MODID).versioned("1");

        registrar.playToServer(LevelUpItemPacket.TYPE, LevelUpItemPacket.STREAM_CODEC, LevelUpItemPacket::handle);
        registrar.playToClient(OpenPlaneteerGUIPacket.TYPE, OpenPlaneteerGUIPacket.STREAM_CODEC, OpenPlaneteerGUIPacket::handle);
        registrar.playToClient(SendPlayerDataPacket.TYPE, SendPlayerDataPacket.STREAM_CODEC, SendPlayerDataPacket::handle);
        registrar.playToServer(BuyItemPacket.TYPE, BuyItemPacket.STREAM_CODEC, BuyItemPacket::handle);
        registrar.playToServer(ApplyAugmentPacket.TYPE, ApplyAugmentPacket.STREAM_CODEC, ApplyAugmentPacket::handle);
        registrar.playToServer(SendItemsToSlotPacket.TYPE, SendItemsToSlotPacket.STREAM_CODEC, SendItemsToSlotPacket::handle);
        registrar.playToServer(LayOffPacket.TYPE, LayOffPacket.STREAM_CODEC, LayOffPacket::handle);
        registrar.playToServer(SellItemPacket.TYPE, SellItemPacket.STREAM_CODEC, SellItemPacket::handle);
    }

    public static <T extends net.minecraft.network.protocol.common.custom.CustomPacketPayload> void sendToPlayer(T message, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, message);
    }

    public static <T extends net.minecraft.network.protocol.common.custom.CustomPacketPayload> void sendToServer(T message) {
        ClientPacketDistributor.sendToServer(message);
    }

    public static <T extends net.minecraft.network.protocol.common.custom.CustomPacketPayload> void sendToAll(T message) {
        PacketDistributor.sendToAllPlayers(message);
    }
}
