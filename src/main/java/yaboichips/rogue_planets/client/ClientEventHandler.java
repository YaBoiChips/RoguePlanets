package yaboichips.rogue_planets.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import yaboichips.rogue_planets.data.ClientPlayerData;

@EventBusSubscriber(Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onRenderGuiLayer(RenderGuiLayerEvent.Post event) {
        if (event.getName().equals(VanillaGuiLayers.PLAYER_HEALTH)) {
            renderIntOnHud(event.getGuiGraphics());
        }
    }

    private static void renderIntOnHud(GuiGraphicsExtractor guiGraphics) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (!mc.player.isLocalPlayer()) return;
        if (mc.player.level().dimension().identifier().getPath().contains("planet")) {
            String text = formatTicksToTime(ClientPlayerData.getO2());
            int x = 5;
            int y = mc.getWindow().getGuiScaledHeight() - 15;
            guiGraphics.text(mc.font, text, x, y, 0xFFFFFF);
        }
    }

    public static String formatTicksToTime(int ticks) {
        int totalSeconds = ticks / 20;
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
