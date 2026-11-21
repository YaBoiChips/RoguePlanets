package yaboichips.rogue_planets.common.items.augments;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

/**
 * Backing storage for the Pockets augment: contents live directly inside the owning item's own
 * NBT, so they travel with it (equip/unequip, inventory moves, even drops) just like a bundle.
 */
public class PocketsContainer extends SimpleContainer {
    private static final String TAG = "Pockets";

    private final ItemStack owner;

    public PocketsContainer(ItemStack owner, int size) {
        super(size);
        this.owner = owner;
        load();
    }

    private void load() {
        if (!owner.hasTag() || !owner.getTag().contains(TAG)) return;
        ListTag list = owner.getTag().getList(TAG, 10);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag tag = list.getCompound(i);
            int slot = tag.getByte("Slot") & 255;
            if (slot >= 0 && slot < getContainerSize()) {
                setItem(slot, ItemStack.of(tag));
            }
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
        ListTag list = new ListTag();
        for (int i = 0; i < getContainerSize(); i++) {
            ItemStack stack = getItem(i);
            if (!stack.isEmpty()) {
                CompoundTag tag = new CompoundTag();
                tag.putByte("Slot", (byte) i);
                stack.save(tag);
                list.add(tag);
            }
        }
        owner.getOrCreateTag().put(TAG, list);
    }
}
