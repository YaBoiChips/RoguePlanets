package yaboichips.rogue_planets.common.items;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import yaboichips.rogue_planets.data.PlayerDataUtils;

public class CreditItem extends Item {
    private final int worth;

    public CreditItem(Properties properties, int worth) {
        super(properties);
        this.worth = worth;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            PlayerDataUtils.addCredits((ServerPlayer) player, worth);
            player.getItemInHand(hand).shrink(1);
        }
        return super.use(level, player, hand);
    }
}
