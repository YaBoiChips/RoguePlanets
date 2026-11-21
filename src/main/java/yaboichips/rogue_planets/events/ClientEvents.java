package yaboichips.rogue_planets.events;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import yaboichips.rogue_planets.RoguePlanets;
import yaboichips.rogue_planets.capabilties.player.ClientPlayerData;
import yaboichips.rogue_planets.client.KeyBindings;
import yaboichips.rogue_planets.client.renderers.GenericMonsterRenderer;
import yaboichips.rogue_planets.client.renderers.HumanRenderer;
import yaboichips.rogue_planets.common.entities.canon.CanonEntityRenderer;
import yaboichips.rogue_planets.common.entities.workers.augmentor.AugmentorScreen;
import yaboichips.rogue_planets.common.entities.workers.ceo.CEOScreen;
import yaboichips.rogue_planets.common.entities.workers.forgemaster.ForgeMasterScreen;
import yaboichips.rogue_planets.common.entities.workers.merchant.MerchantScreen;
import yaboichips.rogue_planets.common.items.augments.PocketsScreen;
import yaboichips.rogue_planets.core.RPBlocks;
import yaboichips.rogue_planets.core.RPEntities;
import yaboichips.rogue_planets.core.RPMenus;

@Mod.EventBusSubscriber(modid = RoguePlanets.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(RPMenus.FORGE_MASTER_MENU.get(), ForgeMasterScreen::new);
            MenuScreens.register(RPMenus.MERCHANT_MENU.get(), MerchantScreen::new);
            MenuScreens.register(RPMenus.AUGMENTOR_MENU.get(), AugmentorScreen::new);
            MenuScreens.register(RPMenus.CEO_MENU.get(), CEOScreen::new);
            MenuScreens.register(RPMenus.POCKETS_MENU.get(), PocketsScreen::new);

            EntityRenderers.register(RPEntities.FORGE_MASTER.get(), HumanRenderer::new);
            EntityRenderers.register(RPEntities.RP_MERCHANT.get(), HumanRenderer::new);
            EntityRenderers.register(RPEntities.AUGMENTOR.get(), HumanRenderer::new);
            EntityRenderers.register(RPEntities.CEO.get(), HumanRenderer::new);
            EntityRenderers.register(RPEntities.CANON.get(), CanonEntityRenderer::new);

            EntityRenderers.register(RPEntities.CYCLOPS.get(), GenericMonsterRenderer::new);
            EntityRenderers.register(RPEntities.ALIEN.get(), GenericMonsterRenderer::new);

            EntityRenderers.register(RPEntities.PORTAPLATFORM.get(), ThrownItemRenderer::new);

            ItemBlockRenderTypes.setRenderLayer(
                    RPBlocks.SPACE_TORCH.get(),
                    RenderType.cutout()
            );
        });
    }

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(KeyBindings.OPEN_POCKETS);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(
                HumanRenderer.LAYER_LOCATION,
                () -> LayerDefinition.create(
                        HumanoidModel.createMesh(CubeDeformation.NONE, 0),
                        64,
                        64
                )
        );
    }
}
