package yaboichips.rogue_planets.common.entities.workers.ceo;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import yaboichips.rogue_planets.capabilties.player.PlayerDataUtils;
import yaboichips.rogue_planets.common.containers.PlanetArmorContainer;
import yaboichips.rogue_planets.common.containers.PlanetInventoryContainer;
import yaboichips.rogue_planets.common.items.LevelableItem;
import yaboichips.rogue_planets.common.items.augments.AugmentType;
import yaboichips.rogue_planets.common.items.augments.AugmentUtil;
import yaboichips.rogue_planets.core.RPItems;
import yaboichips.rogue_planets.core.RPMenus;

import java.util.List;

public class CEOMenu extends AbstractContainerMenu {
    private Container container;

    public Player player;
    private final SimpleContainer levelSlot = new SimpleContainer(1);

    public CEOMenu(int id, Inventory playerInventory, Container container, Container armor) {
        super(RPMenus.CEO_MENU.get(), id);
        checkContainerSize(container, 36);
        checkContainerSize(levelSlot, 1);
        checkContainerSize(armor, 4);
        this.container = container;
        this.player = playerInventory.player;

        this.addSlot(new Slot(levelSlot, 0, 120, 42));

        int slotID = 0;

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(container, slotID, 48 + i * 18, 222));
            slotID++;
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(container, slotID, 48 + j * 18, 164 + i * 18));
                slotID++;
            }
        }
        for (int i = 0; i < 4; i++) {
            this.addSlot(new Slot(armor, i, 48 + i * 18, 141));
        }
    }

    public CEOMenu(int i, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
        this(i, inventory, new SimpleContainer(36), new SimpleContainer(4));
    }

    @Override
    public void removed(Player p_38940_) {
        if (container instanceof SimpleContainer simpleContainer) {
            simpleContainer.addItem(levelSlot.getItem(0));
        }
        super.removed(p_38940_);
    }
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 36) {
                if (!this.moveItemStackTo(itemstack1, 36, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 36, false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }
    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }
    public void layOff() {
        if (player.level().isClientSide()) return;
        ItemStack stack = levelSlot.getItem(0);
        if (stack.isEmpty() || !(stack.getItem() instanceof LevelableItem levelable)) return;

        if (levelable.getLevel(stack) != 20) {
            player.sendSystemMessage(Component.literal("You haven't worked long enough for the lay off, come back to me with a Level 20 Item"));
            return;
        }

        ServerPlayer serverPlayer = (ServerPlayer) player;
        PlayerDataUtils.setInitiated(serverPlayer, false);

        int recovered = dropAugments(serverPlayer);
        if (recovered > 0) {
            player.sendSystemMessage(Component.literal("If you worked longer we would have had to pay you benefits. Here's your " + recovered + " augment(s) back as severance."));
        } else {
            player.sendSystemMessage(Component.literal("If you worked longer we would have had to pay you benefits."));
        }
    }

    /**
     * Strips any augments off every item the player could be carrying them on and drops the
     * matching augment item at their feet, wherever that item currently is (Augmentor storage,
     * real inventory, or actually worn).
     */
    private int dropAugments(ServerPlayer player) {
        int recovered = 0;

        PlanetArmorContainer planetArmor = PlayerDataUtils.getArmorContainer(player);
        if (planetArmor != null) recovered += dropAugmentsFrom(player, planetArmor.getItems());

        PlanetInventoryContainer planetInventory = PlayerDataUtils.getPlanetContainer(player);
        if (planetInventory != null) recovered += dropAugmentsFrom(player, planetInventory.getItems());

        recovered += dropAugmentsFrom(player, player.getInventory().items);
        recovered += dropAugmentsFrom(player, player.getInventory().armor);
        recovered += dropAugmentsFrom(player, player.getInventory().offhand);

        return recovered;
    }

    private int dropAugmentsFrom(ServerPlayer player, List<ItemStack> items) {
        int recovered = 0;
        for (ItemStack stack : items) {
            AugmentType type = AugmentUtil.getAugment(stack);
            if (type == null) continue;
            AugmentUtil.clearAugment(stack);
            player.drop(RPItems.getAugmentItemStack(type), false);
            recovered++;
        }
        return recovered;
    }
}