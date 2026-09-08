package yaboichips.rogue_planets.common.entities.workers.augmentor;

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

public class Augmentor extends HumanMob {
    public Augmentor(EntityType<? extends Mob> type, Level level) {
        super(type, level);
    }

    @Override
    protected InteractionResult mobInteract(Player p, InteractionHand hand) {
        if (!p.level().isClientSide()) {
            if (p instanceof ServerPlayer player) {
                if (!PlayerDataUtils.getIsInitiated(player)) {
                    player.sendSystemMessage(Component.literal("Who are you? I only talk to qualified personal!"));
                } else if (hand == InteractionHand.MAIN_HAND) {
                    player.openMenu(new SimpleMenuProvider((id, playerInv, container) -> new AugmentorMenu(id, playerInv, PlayerDataUtils.getPlanetContainer(player), PlayerDataUtils.getArmorContainer(player)), Component.literal("Forge Master")));
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public Identifier getTextureLocation() {
        return Identifier.fromNamespaceAndPath(MODID, "textures/entity/old_man.png");
    }
}
