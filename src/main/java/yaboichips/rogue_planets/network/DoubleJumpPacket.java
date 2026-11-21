package yaboichips.rogue_planets.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import yaboichips.rogue_planets.common.items.augments.AugmentType;
import yaboichips.rogue_planets.common.items.augments.AugmentUtil;

import java.util.function.Supplier;

public class DoubleJumpPacket {
    private static final double DOUBLE_JUMP_VELOCITY = 0.6;

    public DoubleJumpPacket() {
    }

    public static void encode(DoubleJumpPacket packet, FriendlyByteBuf buf) {
    }

    public static DoubleJumpPacket decode(FriendlyByteBuf buf) {
        return new DoubleJumpPacket();
    }

    public static void handle(DoubleJumpPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null || player.onGround()) return;
            if (AugmentUtil.getAugment(player.getItemBySlot(EquipmentSlot.FEET)) != AugmentType.DOUBLE_JUMP) return;

            Vec3 motion = player.getDeltaMovement();
            player.setDeltaMovement(motion.x, DOUBLE_JUMP_VELOCITY, motion.z);
            player.connection.send(new ClientboundSetEntityMotionPacket(player));
        });
        context.setPacketHandled(true);
    }
}
