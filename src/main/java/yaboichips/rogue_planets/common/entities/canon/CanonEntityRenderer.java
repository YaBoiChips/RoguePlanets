package yaboichips.rogue_planets.common.entities.canon;

import com.geckolib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class CanonEntityRenderer<T extends CanonEntity> extends GeoEntityRenderer<T, EntityRenderState> {

    public CanonEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CanonEntityModel<>());
    }
}
