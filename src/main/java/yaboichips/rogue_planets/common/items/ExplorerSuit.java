package yaboichips.rogue_planets.common.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;
import yaboichips.rogue_planets.data.RPDataComponents;

import java.util.function.Consumer;

public class ExplorerSuit extends Item implements LevelableItem {

    public ExplorerSuit(Properties properties) {
        super(properties);
    }

    public void levelUp(ItemStack stack) {
        setLevel(stack, getLevel(stack) + 1);
        int newMax = (int) Math.round(this.getDefaultMaxDamage() * Math.pow(1.08, getLevel(stack)));
        stack.set(net.minecraft.core.component.DataComponents.MAX_DAMAGE, newMax);
    }

    private int getDefaultMaxDamage() {
        return this.getDefaultInstance().getMaxDamage();
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> components, TooltipFlag flag) {
        components.accept(Component.literal("Level " + getLevel(stack)));
        super.appendHoverText(stack, context, tooltipDisplay, components, flag);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack itemStack = context.getItemInHand();
        boolean activated = itemStack.getOrDefault(RPDataComponents.ARMOR_ACTIVATED.get(), Boolean.FALSE);
        itemStack.set(RPDataComponents.ARMOR_ACTIVATED.get(), !activated);
        if (context.getPlayer() != null) {
            context.getPlayer().sendSystemMessage(Component.literal("Double Armor Capability Activated!"));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
        if (itemStack.getOrDefault(RPDataComponents.ARMOR_ACTIVATED.get(), Boolean.FALSE) && entity instanceof net.minecraft.world.entity.player.Player player) {
            player.sendSystemMessage(Component.literal("Wowie Zowie"));
        }
        super.inventoryTick(itemStack, level, entity, slot);
    }
}
