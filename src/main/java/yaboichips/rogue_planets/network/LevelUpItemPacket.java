package yaboichips.rogue_planets.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import yaboichips.rogue_planets.RoguePlanets;
import yaboichips.rogue_planets.common.entities.workers.forgemaster.ForgeMasterMenu;

public record LevelUpItemPacket() implements CustomPacketPayload {
    public static final Type<LevelUpItemPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(RoguePlanets.MODID, "level_up_item"));
    public static final StreamCodec<RegistryFriendlyByteBuf, LevelUpItemPacket> STREAM_CODEC = StreamCodec.unit(new LevelUpItemPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(LevelUpItemPacket packet, IPayloadContext context) {
        if (context.player() instanceof ServerPlayer serverPlayer && serverPlayer.containerMenu instanceof ForgeMasterMenu menu) {
            menu.levelUpItem();
        }
    }
}
