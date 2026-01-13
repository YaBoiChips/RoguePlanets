package yaboichips.rogue_planets.common.world.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import yaboichips.rogue_planets.core.RPBlocks;

public class VoidstoneSpikeFeature extends Feature<NoneFeatureConfiguration> {

    public VoidstoneSpikeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        boolean stalactite = random.nextBoolean();
        Direction direction = stalactite ? Direction.DOWN : Direction.UP;

        // Determine starting position
        BlockPos start = findSurface(level, origin, direction);
        if (start == null) return false;

        int baseHeight = random.nextInt(3) + 1;
        int middleHeight = random.nextInt(3) + 1;
        int tipHeight = random.nextInt(3) + 1;

        BlockPos.MutableBlockPos pos = start.mutable();

        // Base
        for (int i = 0; i < baseHeight; i++) {
            if (!canPlace(level, pos)) return false;
            level.setBlock(pos, RPBlocks.VOIDSTONE.get().defaultBlockState(), 2);
            pos.move(direction);
        }

        // Middle
        for (int i = 0; i < middleHeight; i++) {
            if (!canPlace(level, pos)) return false;
            level.setBlock(pos, RPBlocks.VOIDSTONE_SPIKE_MIDDLE.get().defaultBlockState(), 2);
            pos.move(direction);
        }

        // Tip
        for (int i = 0; i < tipHeight; i++) {
            if (!canPlace(level, pos)) return false;
            level.setBlock(pos, RPBlocks.VOIDSTONE_SPIKE_TOP.get().defaultBlockState(), 2);
            pos.move(direction);
        }

        return true;
    }

    private boolean canPlace(LevelAccessor level, BlockPos pos) {
        return level.getBlockState(pos).isAir();
    }

    private BlockPos findSurface(LevelAccessor level, BlockPos origin, Direction direction) {
        BlockPos.MutableBlockPos pos = origin.mutable();

        for (int i = 0; i < 12; i++) {
            BlockState state = level.getBlockState(pos);
            BlockState next = level.getBlockState(pos.relative(direction.getOpposite()));

            if (state.isAir() && next.isSolid()) {
                return pos.immutable();
            }

            pos.move(direction);
        }
        return null;
    }
}
