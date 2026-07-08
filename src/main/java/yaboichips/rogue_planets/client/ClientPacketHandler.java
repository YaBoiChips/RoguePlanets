package yaboichips.rogue_planets.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yaboichips.rogue_planets.client.screens.PlaneteerInfoScreen;
import yaboichips.rogue_planets.network.OpenPlaneteerGUIPacket;

@OnlyIn(Dist.CLIENT)
public class ClientPacketHandler {
    public static void handle(OpenPlaneteerGUIPacket packet){
        // This code runs on the client side
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player != null) {
            // Open the GUI
            mc.setScreen(new PlaneteerInfoScreen(packet.credits));
        }
    }
}
