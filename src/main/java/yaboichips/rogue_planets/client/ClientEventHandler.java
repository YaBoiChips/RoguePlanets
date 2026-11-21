package yaboichips.rogue_planets.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yaboichips.rogue_planets.capabilties.player.ClientPlayerData;
import yaboichips.rogue_planets.common.items.augments.AugmentType;
import yaboichips.rogue_planets.common.items.augments.AugmentUtil;
import yaboichips.rogue_planets.network.DoubleJumpPacket;
import yaboichips.rogue_planets.network.OpenPocketsPacket;
import yaboichips.rogue_planets.network.RoguePackets;

import static yaboichips.rogue_planets.RoguePlanets.MODID;

@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEventHandler {
    private static final int DOUBLE_JUMP_AIRBORNE_TICKS = 20;

    private static int airborneTicks = 0;
    private static boolean usedDoubleJump = false;
    private static boolean wasJumpKeyDown = false;

    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent event) {
        if (event.getOverlay() == VanillaGuiOverlay.PLAYER_HEALTH.type()) {
            renderIntOnHud(event.getGuiGraphics());
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        if (player.onGround()) {
            airborneTicks = 0;
            usedDoubleJump = false;
        } else {
            airborneTicks++;
        }

        boolean jumpKeyDown = mc.options.keyJump.isDown();
        boolean justPressed = jumpKeyDown && !wasJumpKeyDown;
        wasJumpKeyDown = jumpKeyDown;

        if (justPressed && !player.onGround() && airborneTicks > DOUBLE_JUMP_AIRBORNE_TICKS && !usedDoubleJump) {
            ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
            if (AugmentUtil.getAugment(boots) == AugmentType.DOUBLE_JUMP) {
                usedDoubleJump = true;
                RoguePackets.CHANNEL.sendToServer(new DoubleJumpPacket());
            }
        }

        while (KeyBindings.OPEN_POCKETS.consumeClick()) {
            RoguePackets.CHANNEL.sendToServer(new OpenPocketsPacket());
        }
    }

    private static void renderIntOnHud(GuiGraphics guiGraphics) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (!mc.player.isLocalPlayer()) return;
        if (mc.player.level().dimension().location().getPath().contains("planet")) {
            String text = formatTicksToTime(ClientPlayerData.getO2());
            int x = 5;
            int y = mc.getWindow().getGuiScaledHeight() - 15;
            RenderSystem.enableBlend();
            guiGraphics.drawString(mc.font, text, x, y, 0xFFFFFF);
            RenderSystem.disableBlend();
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
