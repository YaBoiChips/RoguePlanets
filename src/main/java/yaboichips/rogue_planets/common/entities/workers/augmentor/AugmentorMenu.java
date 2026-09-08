package yaboichips.rogue_planets.common.entities.workers.augmentor;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import yaboichips.rogue_planets.common.containers.AugmentContainer;
import yaboichips.rogue_planets.common.items.augments.AugmentItem;
import yaboichips.rogue_planets.core.RPMenus;
import yaboichips.rogue_planets.data.AugmentData;
import yaboichips.rogue_planets.data.RPDataComponents;

public class AugmentorMenu extends AbstractContainerMenu {
    private final SimpleContainer container;

    public Player player;
    private final SimpleContainer augmentableSlot = new SimpleContainer(1);
    private AugmentContainer liveAugments;
    private int liveAugmentSlots;

    public AugmentorMenu(int id, Inventory playerInventory, SimpleContainer container, SimpleContainer armor) {
        super(RPMenus.AUGMENTOR_MENU.get(), id);
        checkContainerSize(container, 36);
        checkContainerSize(armor, 4);
        checkContainerSize(augmentableSlot, 1);
        this.container = container;
        this.player = playerInventory.player;

        this.addSlot(new Slot(augmentableSlot, 0, 120, 44) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.has(RPDataComponents.AUGMENT_DATA.get());
            }

            @Override
            public boolean mayPickup(Player p_40228_) {
                return false;
            }

            @Override
            public void setChanged() {
                super.setChanged();
                onSlotChanged(this);
            }
        });

        int k;
        for (k = 0; k < 3; ++k) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + k * 9 + 9, 48 + j * 18, 142 + k * 18));
            }
        }

        int slotID = 0;

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(container, slotID, 48 + i * 18, 232));
            slotID++;
        }
        for (int i = 0; i < 4; i++) {
            this.addSlot(new Slot(armor, i, 48 + i * 18, 214));
            slotID++;
        }
    }

    public AugmentorMenu(int i, Inventory inventory, RegistryFriendlyByteBuf buf) {
        this(i, inventory, new SimpleContainer(36), new SimpleContainer(4));
    }

    public void onClose() {
        writeBackAugments();
        ItemStack stack = augmentableSlot.getItem(0);
        container.addItem(stack);
    }

    private void onSlotChanged(Slot slot) {
        ItemStack stack = slot.getItem();
        AugmentData data = stack.get(RPDataComponents.AUGMENT_DATA.get());
        if (data != null) {
            this.liveAugments = AugmentContainer.fromAugmentData(data, 3);
            this.liveAugmentSlots = data.augmentSlots();
            for (int i = 0; i < data.augmentSlots(); i++) {
                Slot dynamicSlot = new Slot(liveAugments, i, 102 + i * 18, 94) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return stack.getItem() instanceof AugmentItem;
                    }

                    @Override
                    public void setChanged() {
                        super.setChanged();
                        writeBackAugments();
                    }
                };
                this.addSlot(dynamicSlot);
            }
        }
    }

    private void writeBackAugments() {
        ItemStack stack = augmentableSlot.getItem(0);
        if (!stack.isEmpty() && liveAugments != null) {
            stack.set(RPDataComponents.AUGMENT_DATA.get(), liveAugments.toAugmentData(liveAugmentSlots));
        }
    }

    public void applyAugment() {
        writeBackAugments();
        ItemStack stack = augmentableSlot.getItem(0);
        container.addItem(stack);
    }

    @Override
    public ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }
}
