package yaboichips.rogue_planets.common.world;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Per-dimension terrain seed overrides for the planet dimensions Infiniverse creates at
 * runtime. The server's real world seed (what the overworld, nether, end and any datapack
 * dimension use) is never touched - a dimension only gets a different seed if it has an entry
 * here, which {@code ServerLevelMixin} consults from {@link net.minecraft.server.level.ServerLevel#getSeed()}.
 * Register a seed (via {@link #set}) before the dimension is actually constructed, since chunk
 * generation reads the seed the moment the level's ChunkMap is built.
 */
public class DimensionSeeds {
    private static final Map<ResourceKey<Level>, Long> SEEDS = new ConcurrentHashMap<>();

    private DimensionSeeds() {
    }

    public static void set(ResourceKey<Level> dimension, long seed) {
        SEEDS.put(dimension, seed);
    }

    public static boolean has(ResourceKey<Level> dimension) {
        return SEEDS.containsKey(dimension);
    }

    public static long get(ResourceKey<Level> dimension, long fallback) {
        return SEEDS.getOrDefault(dimension, fallback);
    }

    public static void remove(ResourceKey<Level> dimension) {
        SEEDS.remove(dimension);
    }
}
