package yaboichips.rogue_planets.common.items;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import yaboichips.rogue_planets.data.PlayerDataUtils;
import yaboichips.rogue_planets.network.OpenPlaneteerGUIPacket;
import yaboichips.rogue_planets.network.RoguePackets;

public class PlaneteerManuel extends Item {

    public PlaneteerManuel(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (player instanceof ServerPlayer sPlayer) {
            int credits = PlayerDataUtils.getCredits(sPlayer);
            RoguePackets.sendToPlayer(new OpenPlaneteerGUIPacket(credits), sPlayer);
        }
        return super.use(level, player, hand);
    }
}
