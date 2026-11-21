package yaboichips.rogue_planets.common.entities.workers.augmentor;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import yaboichips.rogue_planets.common.containers.PlanetArmorContainer;
import yaboichips.rogue_planets.common.containers.PlanetInventoryContainer;
import yaboichips.rogue_planets.common.items.augments.AugmentItem;
import yaboichips.rogue_planets.common.items.augments.AugmentTarget;
import yaboichips.rogue_planets.common.items.augments.AugmentType;
import yaboichips.rogue_planets.common.items.augments.AugmentUtil;
import yaboichips.rogue_planets.core.RPMenus;

public class AugmentorMenu extends AbstractContainerMenu {
    public static final int PREVIEW_SLOT = 0;
    public static final int AUGMENT_SLOT = 1;
    public static final int INV_SLOT_START = 2;
    public static final int INV_SLOT_END = INV_SLOT_START + 36;

    private final PlanetArmorContainer armor;
    private final PlanetInventoryContainer planetInventory;
    private final SimpleContainer augmentContainer = new SimpleContainer(1);
    private final SimpleContainer previewContainer = new SimpleContainer(1);

    public final Player player;
    @Nullable
    private AugmentTarget selectedTarget;

    public AugmentorMenu(int id, Inventory playerInventory, PlanetInventoryContainer planetInventory, PlanetArmorContainer armor) {
        super(RPMenus.AUGMENTOR_MENU.get(), id);
        this.planetInventory = planetInventory;
        this.armor = armor;
        this.player = playerInventory.player;

        // Centered horizontally over the Apply Augment button (x + 128, width 110) in AugmentorScreen.
        int itemColumnX = 174;

        this.addSlot(new Slot(previewContainer, 0, itemColumnX, 24) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public boolean mayPickup(Player player) {
                return false;
            }
        });

        this.addSlot(new Slot(augmentContainer, 0, itemColumnX, 58) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof AugmentItem;
            }
        });

        // Centered horizontally in the 256-wide panel: (256 - 9 * 18) / 2 = 47.
        int inventoryX = 47;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, inventoryX + col * 18, 140 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, inventoryX + col * 18, 198));
        }
    }

    public AugmentorMenu(int id, Inventory inventory, FriendlyByteBuf buf) {
        this(id, inventory, new PlanetInventoryContainer(), new PlanetArmorContainer());
    }

    public void selectTarget(AugmentTarget target) {
        this.selectedTarget = target;
        refreshPreview();
    }

    private void refreshPreview() {
        ItemStack preview = ItemStack.EMPTY;
        if (selectedTarget != null) {
            ItemStack target = AugmentUtil.getTargetItem(selectedTarget, armor, planetInventory);
            if (!target.isEmpty()) {
                preview = target.copy();
            }
        }
        previewContainer.setItem(0, preview);
    }

    public void applyAugment() {
        if (player.level().isClientSide()) return;
        if (selectedTarget == null) {
            player.sendSystemMessage(Component.literal("Select an item to augment first."));
            return;
        }
        ItemStack targetItem = AugmentUtil.getTargetItem(selectedTarget, armor, planetInventory);
        if (targetItem.isEmpty()) {
            player.sendSystemMessage(Component.literal("You don't have that item."));
            return;
        }
        if (AugmentUtil.hasAugment(targetItem)) {
            player.sendSystemMessage(Component.literal("That item already has an augment installed."));
            return;
        }
        ItemStack augmentStack = augmentContainer.getItem(0);
        if (!(augmentStack.getItem() instanceof AugmentItem augmentItem) || !augmentItem.getType().isValidFor(selectedTarget)) {
            player.sendSystemMessage(Component.literal("Place a matching augment in the slot first."));
            return;
        }

        AugmentType type = augmentItem.getType();
        AugmentUtil.setAugment(targetItem, type);
        augmentStack.shrink(1);
        player.sendSystemMessage(Component.literal("Installed " + type.getDisplayName() + "."));
        refreshPreview();
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        ItemStack leftover = augmentContainer.removeItemNoUpdate(0);
        if (!leftover.isEmpty() && !player.getInventory().add(leftover)) {
            player.drop(leftover, false);
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack original = slot.getItem();
        ItemStack copy = original.copy();

        if (index == PREVIEW_SLOT) {
            return ItemStack.EMPTY;
        } else if (index == AUGMENT_SLOT) {
            if (!this.moveItemStackTo(original, INV_SLOT_START, INV_SLOT_END, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (!(original.getItem() instanceof AugmentItem)) return ItemStack.EMPTY;
            if (!this.moveItemStackTo(original, AUGMENT_SLOT, AUGMENT_SLOT + 1, false)) {
                return ItemStack.EMPTY;
            }
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
        return planetInventory.stillValid(player);
    }
}
