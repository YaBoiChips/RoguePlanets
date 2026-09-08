package yaboichips.rogue_planets.events;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import yaboichips.rogue_planets.client.renderers.GenericMonsterRenderer;
import yaboichips.rogue_planets.client.renderers.HumanRenderer;
import yaboichips.rogue_planets.common.entities.canon.CanonEntityRenderer;
import yaboichips.rogue_planets.common.entities.workers.augmentor.AugmentorScreen;
import yaboichips.rogue_planets.common.entities.workers.ceo.CEOScreen;
import yaboichips.rogue_planets.common.entities.workers.forgemaster.ForgeMasterScreen;
import yaboichips.rogue_planets.common.entities.workers.merchant.MerchantScreen;
import yaboichips.rogue_planets.core.RPEntities;
import yaboichips.rogue_planets.core.RPMenus;

@EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(RPMenus.FORGE_MASTER_MENU.get(), ForgeMasterScreen::new);
        event.register(RPMenus.MERCHANT_MENU.get(), MerchantScreen::new);
        event.register(RPMenus.AUGMENTOR_MENU.get(), AugmentorScreen::new);
        event.register(RPMenus.CEO_MENU.get(), CEOScreen::new);
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            EntityRenderers.register(RPEntities.FORGE_MASTER.get(), HumanRenderer::new);
            EntityRenderers.register(RPEntities.RP_MERCHANT.get(), HumanRenderer::new);
            EntityRenderers.register(RPEntities.AUGMENTOR.get(), HumanRenderer::new);
            EntityRenderers.register(RPEntities.CEO.get(), HumanRenderer::new);
            EntityRenderers.register(RPEntities.CANON.get(), CanonEntityRenderer::new);

            EntityRenderers.register(RPEntities.CYCLOPS.get(), GenericMonsterRenderer::new);
            EntityRenderers.register(RPEntities.ALIEN.get(), GenericMonsterRenderer::new);

            EntityRenderers.register(RPEntities.PORTAPLATFORM.get(), ThrownItemRenderer::new);
        });
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
