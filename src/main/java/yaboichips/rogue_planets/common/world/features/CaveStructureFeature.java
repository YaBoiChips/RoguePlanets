package yaboichips.rogue_planets.common.world.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.List;

// Placed as a feature - not a jigsaw structure - on purpose: a structure's position is chosen during the
// STRUCTURE_STARTS chunk status, which always runs before the biome's cave carvers (CARVERS status), so at
// that point there is no real cave air anywhere to check. Features run in the FEATURES status, strictly
// after CARVERS, so by the time this runs the carvers have already dug real caves and level.getBlockState
// reflects them - that's what makes an actual air/solid check possible here.
//
// Shared by every NBT "sits inside a cave pocket" structure in this mod (small/large camps, small/large
// houses) - only the numbers differ per structure, driven entirely by CaveStructureFeatureConfiguration.
public class CaveStructureFeature extends Feature<CaveStructureFeatureConfiguration> {

    public CaveStructureFeature(Codec<CaveStructureFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<CaveStructureFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        CaveStructureFeatureConfiguration config = context.config();

        if (!hasRequiredAirPocket(level, origin, config)) {
            return false; // do not generate the structure if the required air isn't there
        }

        List<ResourceLocation> templates = config.templates;
        RandomSource random = context.random();
        StructureTemplateManager templateManager = level.getLevel().getStructureManager();
        StructureTemplate template = templateManager.getOrCreate(templates.get(random.nextInt(templates.size())));

        BlockPos placement = origin.offset(0, -config.groundInset, 0);
        StructurePlaceSettings settings = new StructurePlaceSettings()
                .setRotation(Rotation.NONE)
                .setMirror(Mirror.NONE)
                .setIgnoreEntities(false)
                .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);

        return template.placeInWorld(level, placement, placement, settings, random, 2);
    }

    // the block directly beneath the origin must be solid ground, and the whole volume from the origin
    // looking up (airAboveOrigin blocks) and extending east/south (clearEast/clearSouth blocks - the
    // directions the structure's footprint grows from its north-west origin) must already be open air.
    // This runs after the biome's carvers have dug the dimension's caves, so this is real cave air.
    private boolean hasRequiredAirPocket(WorldGenLevel level, BlockPos origin, CaveStructureFeatureConfiguration config) {
        if (!level.getBlockState(origin.below()).isSolid()) {
            return false;
        }

        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int dy = 0; dy < config.airAboveOrigin; dy++) {
            for (int dx = 0; dx <= config.clearEast; dx++) {
                for (int dz = 0; dz <= config.clearSouth; dz++) {
                    cursor.set(origin.getX() + dx, origin.getY() + dy, origin.getZ() + dz);
                    if (!level.getBlockState(cursor).isAir()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
