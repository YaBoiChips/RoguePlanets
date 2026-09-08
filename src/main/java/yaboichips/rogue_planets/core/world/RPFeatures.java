package yaboichips.rogue_planets.core.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import yaboichips.rogue_planets.common.world.features.AngledArchFeature;
import yaboichips.rogue_planets.common.world.features.BeegStoneSpike;
import yaboichips.rogue_planets.common.world.features.CaveStructureFeature;
import yaboichips.rogue_planets.common.world.features.CaveStructureFeatureConfiguration;
import yaboichips.rogue_planets.common.world.features.VoidstoneSpikeFeature;

import static yaboichips.rogue_planets.RoguePlanets.MODID;

public class RPFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, MODID);

    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> BEEG_SPIKE = FEATURES.register("beeg_spike", () -> new BeegStoneSpike(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> ANGLED_ARCH = FEATURES.register("angled_arch", () -> new AngledArchFeature(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> VOIDSTONE_STALAGMITES = FEATURES.register("voidstone_stalagmites", () -> new VoidstoneSpikeFeature(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<CaveStructureFeatureConfiguration>> CAVE_STRUCTURE = FEATURES.register("cave_structure", () -> new CaveStructureFeature(CaveStructureFeatureConfiguration.CODEC));
}
