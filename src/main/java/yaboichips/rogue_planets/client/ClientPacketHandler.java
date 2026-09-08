package yaboichips.rogue_planets.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import yaboichips.rogue_planets.client.screens.PlaneteerInfoScreen;
import yaboichips.rogue_planets.network.OpenPlaneteerGUIPacket;


public class ClientPacketHandler {
    public static void handle(OpenPlaneteerGUIPacket packet) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player != null) {
            mc.setScreen(new PlaneteerInfoScreen(packet.credits()));
        }
    }
}
