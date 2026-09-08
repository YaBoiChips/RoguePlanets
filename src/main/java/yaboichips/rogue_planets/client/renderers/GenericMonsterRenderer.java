package yaboichips.rogue_planets.client.renderers;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import yaboichips.rogue_planets.common.entities.monsters.GenericMonster;

public class GenericMonsterRenderer extends MobRenderer<GenericMonster, TexturedHumanoidRenderState, HumanoidModel<TexturedHumanoidRenderState>> {

    public GenericMonsterRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(HumanRenderer.LAYER_LOCATION)), 0.3f);
    }

    @Override
    public TexturedHumanoidRenderState createRenderState() {
        return new TexturedHumanoidRenderState();
    }

    @Override
    public void extractRenderState(GenericMonster entity, TexturedHumanoidRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.texture = entity.getTextureLocation();
    }

    @Override
    public Identifier getTextureLocation(TexturedHumanoidRenderState state) {
        return state.texture;
    }
}
