package yaboichips.rogue_planets.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import yaboichips.rogue_planets.RoguePlanets;
import yaboichips.rogue_planets.common.entities.workers.merchant.MerchantMenu;

public record SellItemPacket() implements CustomPacketPayload {
    public static final Type<SellItemPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(RoguePlanets.MODID, "sell_item"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SellItemPacket> STREAM_CODEC = StreamCodec.unit(new SellItemPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SellItemPacket packet, IPayloadContext context) {
        if (context.player() instanceof ServerPlayer serverPlayer && serverPlayer.containerMenu instanceof MerchantMenu menu) {
            menu.buyFromPlayer();
        }
    }
}
