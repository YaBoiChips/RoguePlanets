package yaboichips.rogue_planets.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import yaboichips.rogue_planets.RoguePlanets;
import yaboichips.rogue_planets.common.entities.workers.merchant.MerchantMenu;

public record BuyItemPacket(ItemStack itemStack, int value) implements CustomPacketPayload {
    public static final Type<BuyItemPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(RoguePlanets.MODID, "buy_item"));
    public static final StreamCodec<RegistryFriendlyByteBuf, BuyItemPacket> STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_STREAM_CODEC, BuyItemPacket::itemStack,
            ByteBufCodecs.VAR_INT, BuyItemPacket::value,
            BuyItemPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(BuyItemPacket packet, IPayloadContext context) {
        if (context.player() instanceof ServerPlayer serverPlayer && serverPlayer.containerMenu instanceof MerchantMenu menu) {
            menu.purchaseItem(packet.itemStack(), packet.value());
        }
    }
}
