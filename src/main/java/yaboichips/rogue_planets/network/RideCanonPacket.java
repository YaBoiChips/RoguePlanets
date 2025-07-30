package yaboichips.rogue_planets.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RideCanonPacket {
    private final BlockPos blockEntityPos;

    public RideCanonPacket(BlockPos pos) {
        this.blockEntityPos = pos;
    }

    public static void encode(RideCanonPacket packet, FriendlyByteBuf buf) {
        buf.writeBlockPos(packet.blockEntityPos);
    }

    // Decode data from the buffer
    public static RideCanonPacket decode(FriendlyByteBuf buf) {
        return new RideCanonPacket(buf.readBlockPos());
    }

    public static void handle(RideCanonPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        ServerPlayer player = context.getSender();
        context.setPacketHandled(true);
    }
}
