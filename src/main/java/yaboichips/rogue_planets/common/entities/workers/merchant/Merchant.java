package yaboichips.rogue_planets.common.entities.workers.merchant;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import yaboichips.rogue_planets.common.entities.workers.HumanMob;
import yaboichips.rogue_planets.data.PlayerDataUtils;

import static yaboichips.rogue_planets.RoguePlanets.MODID;

public class Merchant extends HumanMob {
    public Merchant(EntityType<? extends Mob> mob, Level level) {
        super(mob, level);
    }

    @Override
    protected InteractionResult mobInteract(Player p, InteractionHand hand) {
        if (!p.level().isClientSide()) {
            if (p instanceof ServerPlayer player) {
                if (!PlayerDataUtils.getIsInitiated(player)) {
                    player.sendSystemMessage(Component.literal("Who are you? I only sell to qualified personal!"));
                } else if (hand == InteractionHand.MAIN_HAND) {
                    player.openMenu(new SimpleMenuProvider((id, playerInv, container) -> new MerchantMenu(id, playerInv, PlayerDataUtils.getPlanetContainer(player), PlayerDataUtils.getArmorContainer(player)), Component.literal("Merchant")));
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public Identifier getTextureLocation() {
        return Identifier.fromNamespaceAndPath(MODID, "textures/entity/merchant.png");
    }
}
