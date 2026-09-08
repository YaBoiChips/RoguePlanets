package yaboichips.rogue_planets.client.renderers;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import yaboichips.rogue_planets.common.entities.workers.HumanMob;

import static yaboichips.rogue_planets.RoguePlanets.MODID;

public class HumanRenderer extends MobRenderer<HumanMob, TexturedHumanoidRenderState, HumanoidModel<TexturedHumanoidRenderState>> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(MODID, "human_model"), "main");

    public HumanRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(LAYER_LOCATION)), 0.3f);
    }

    @Override
    public TexturedHumanoidRenderState createRenderState() {
        return new TexturedHumanoidRenderState();
    }

    @Override
    public void extractRenderState(HumanMob entity, TexturedHumanoidRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.texture = entity.getTextureLocation();
    }

    @Override
    public Identifier getTextureLocation(TexturedHumanoidRenderState state) {
        return state.texture;
    }
}
