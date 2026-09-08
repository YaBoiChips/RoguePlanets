package yaboichips.rogue_planets.common.entities.workers.merchant;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import yaboichips.rogue_planets.core.RPItems;
import yaboichips.rogue_planets.core.RPMenus;
import yaboichips.rogue_planets.data.PlayerDataUtils;

import java.util.Set;

public class MerchantMenu extends AbstractContainerMenu {

    private final SimpleContainer container;

    public Player player;

    public Slot sellSlot;

    public MerchantMenu(int id, Inventory playerInventory, SimpleContainer container, Container armor) {
        super(RPMenus.MERCHANT_MENU.get(), id);
        checkContainerSize(container, 36);
        this.container = container;
        this.player = playerInventory.player;
        int slotID = 0;

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(container, slotID, 88 + i * 18, 232));
            slotID++;
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(container, slotID, 88 + j * 18, 174 + i * 18));
                slotID++;
            }
        }
        for (int i = 0; i < 4; i++) {
            this.addSlot(new Slot(armor, i, 88 + i * 18, 151));
        }
        sellSlot = new Slot(new SimpleContainer(1), 0, 160, 58);
        this.addSlot(sellSlot);
    }

    public MerchantMenu(int i, Inventory inventory, RegistryFriendlyByteBuf buf) {
        this(i, inventory, new SimpleContainer(36), new SimpleContainer(4));
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

    public void purchaseItem(ItemStack stack, int price) {
        if (!player.level().isClientSide()) {
            if (PlayerDataUtils.getCredits((ServerPlayer) player) >= price) {
                if (stack.is(RPItems.PLANETEER_PICKAXE.get())) {
                    if (container.hasAnyOf(Set.of(stack.getItem()))) {
                        player.sendSystemMessage(Component.literal("You already have a pickaxe"));
                    } else {
                        player.sendSystemMessage(Component.literal("Don't Lose this one u BOZO"));
                        this.container.addItem(stack);
                    }
                } else {
                    this.container.addItem(stack);
                    PlayerDataUtils.subCredits((ServerPlayer) player, price);
                }
            }
        } else {
            player.sendSystemMessage(Component.literal("You need " + (price - PlayerDataUtils.getCredits((ServerPlayer) player)) + " more Credits"));
        }
    }

    public void buyFromPlayer() {
        for (MerchantBuy buy : MerchantBuy.BUYS) {
            ItemStack stack = sellSlot.getItem();
            Item item = stack.getItem();
            if (item == buy.item().getItem()) {
                PlayerDataUtils.addCredits((ServerPlayer) player, buy.price() * stack.getCount());
                stack.shrink(stack.getCount());
                break;
            }
        }
    }

    public String getCredits(ServerPlayer player) {
        return String.valueOf(PlayerDataUtils.getCredits(player));
    }
}
