package yaboichips.rogue_planets.common.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;

public class CaveStructureFeatureConfiguration implements FeatureConfiguration {
    public static final Codec<CaveStructureFeatureConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.listOf().fieldOf("templates").forGetter(c -> c.templates),
            Codec.INT.fieldOf("ground_inset").forGetter(c -> c.groundInset),
            Codec.INT.fieldOf("air_above_origin").forGetter(c -> c.airAboveOrigin),
            Codec.INT.fieldOf("clear_east").forGetter(c -> c.clearEast),
            Codec.INT.fieldOf("clear_south").forGetter(c -> c.clearSouth)
    ).apply(instance, CaveStructureFeatureConfiguration::new));

    public final List<ResourceLocation> templates;
    public final int groundInset; // how many blocks below the origin the structure is placed; 0 = not inset
    public final int airAboveOrigin; // required clear air looking up from the origin
    public final int clearEast; // required clear air extending east (+X) from the origin
    public final int clearSouth; // required clear air extending south (+Z) from the origin

    public CaveStructureFeatureConfiguration(List<ResourceLocation> templates, int groundInset, int airAboveOrigin, int clearEast, int clearSouth) {
        this.templates = templates;
        this.groundInset = groundInset;
        this.airAboveOrigin = airAboveOrigin;
        this.clearEast = clearEast;
        this.clearSouth = clearSouth;
    }
}
