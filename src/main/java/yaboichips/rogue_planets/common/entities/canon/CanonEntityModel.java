package yaboichips.rogue_planets.common.entities.canon;

import com.geckolib.animatable.GeoAnimatable;
import com.geckolib.model.GeoModel;
import com.geckolib.renderer.base.GeoRenderState;
import net.minecraft.resources.Identifier;

import static yaboichips.rogue_planets.RoguePlanets.MODID;

public class CanonEntityModel<T extends GeoAnimatable> extends GeoModel<T> {
    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return Identifier.fromNamespaceAndPath(MODID, "geo/canon.geo.json");
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return Identifier.fromNamespaceAndPath(MODID, "textures/entity/canon.png");
    }

    @Override
    public Identifier getAnimationResource(T t) {
        return null;
    }
}
