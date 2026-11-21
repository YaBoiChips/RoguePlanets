package yaboichips.rogue_planets.common.items.augments;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import yaboichips.rogue_planets.core.RPMenus;

public class PocketsMenu extends AbstractContainerMenu {
    public static final int SIZE = 9;

    private final PocketsContainer pockets;

    public PocketsMenu(int id, Inventory playerInventory, ItemStack owner) {
        super(RPMenus.POCKETS_MENU.get(), id);
        this.pockets = new PocketsContainer(owner.isEmpty() ? new ItemStack(Items.AIR) : owner, SIZE);

        for (int i = 0; i < SIZE; i++) {
            this.addSlot(new Slot(pockets, i, 62 + i * 18, 20));
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 58 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 116));
        }
    }

    public PocketsMenu(int id, Inventory inventory, FriendlyByteBuf buf) {
        this(id, inventory, ItemStack.EMPTY);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack original = slot.getItem();
        ItemStack copy = original.copy();

        if (index < SIZE) {
            if (!this.moveItemStackTo(original, SIZE, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (!this.moveItemStackTo(original, 0, SIZE, false)) {
            return ItemStack.EMPTY;
        }

        if (original.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return copy;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
