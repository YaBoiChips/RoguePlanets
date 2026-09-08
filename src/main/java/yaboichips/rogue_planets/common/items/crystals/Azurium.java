package yaboichips.rogue_planets.common.items.crystals;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import yaboichips.rogue_planets.data.PlayerDataUtils;

public class Azurium extends Item {

    public Azurium(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player instanceof ServerPlayer serverPlayer) {
            // MAKE A POTION EFFECT!
            PlayerDataUtils.addO2(serverPlayer, 30 * 20);
        }
        player.getCooldowns().addCooldown(stack, 30);
        stack.shrink(1);
        return InteractionResult.CONSUME;
    }
}
