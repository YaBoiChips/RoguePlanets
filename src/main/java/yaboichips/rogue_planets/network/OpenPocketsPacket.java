package yaboichips.rogue_planets.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import yaboichips.rogue_planets.common.items.augments.AugmentType;
import yaboichips.rogue_planets.common.items.augments.AugmentUtil;
import yaboichips.rogue_planets.common.items.augments.PocketsMenu;

import java.util.function.Supplier;

public class OpenPocketsPacket {
    public OpenPocketsPacket() {
    }

    public static void encode(OpenPocketsPacket packet, FriendlyByteBuf buf) {
    }

    public static OpenPocketsPacket decode(FriendlyByteBuf buf) {
        return new OpenPocketsPacket();
    }

    public static void handle(OpenPocketsPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
            ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
            ItemStack owner;
            if (AugmentUtil.getAugment(chest) == AugmentType.POCKETS) {
                owner = chest;
            } else if (AugmentUtil.getAugment(legs) == AugmentType.POCKETS) {
                owner = legs;
            } else {
                player.sendSystemMessage(Component.literal("You need the Pockets augment on your Jacket or Pantaloons."));
                return;
            }

            player.openMenu(new SimpleMenuProvider((id, inv, p) -> new PocketsMenu(id, inv, owner), Component.literal("Pockets")));
        });
        context.setPacketHandled(true);
    }
}
