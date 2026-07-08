package yaboichips.rogue_planets.core.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.ReplaceBlobsFeature;
import net.minecraft.world.level.levelgen.feature.ReplaceBlockFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ReplaceBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ReplaceSphereConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import yaboichips.rogue_planets.common.world.features.AngledArchFeature;
import yaboichips.rogue_planets.common.world.features.BeegStoneSpike;
import yaboichips.rogue_planets.common.world.features.VoidstoneSpikeFeature;

import static yaboichips.rogue_planets.RoguePlanets.MODID;

public class RPFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, MODID);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> BEEG_SPIKE = FEATURES.register("beeg_spike", () -> new BeegStoneSpike(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> ANGLED_ARCH = FEATURES.register("angled_arch", () -> new AngledArchFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> VOIDSTONE_STALAGMITES = FEATURES.register("voidstone_stalagmites", () -> new VoidstoneSpikeFeature(NoneFeatureConfiguration.CODEC));
//    public static final RegistryObject<Feature<ReplaceBlockConfiguration>> MOSSY_VOIDSTONE_BLOBS = FEATURES.register("voidstone_blobs", () -> new ReplaceBlockFeature(ReplaceBlockConfiguration.CODEC));

}
