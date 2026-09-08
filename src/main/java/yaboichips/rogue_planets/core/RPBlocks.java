package yaboichips.rogue_planets.core;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import yaboichips.rogue_planets.common.blocks.RopeBlock;
import yaboichips.rogue_planets.common.blocks.SpikeMiddle;
import yaboichips.rogue_planets.common.blocks.SpikeTop;
import yaboichips.rogue_planets.common.items.SpaceTorch;

import java.util.function.Function;

import static yaboichips.rogue_planets.RoguePlanets.MODID;

public class RPBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, MODID);

    private static ResourceKey<Block> blockKey(Identifier id) {
        return ResourceKey.create(Registries.BLOCK, id);
    }

    public static final DeferredHolder<Block, RopeBlock> ROPE = BLOCKS.register("rope", id -> new RopeBlock(BlockBehaviour.Properties.of().setId(blockKey(id)).mapColor(MapColor.COLOR_YELLOW).sound(SoundType.WOOL).noCollision()));
    public static final DeferredHolder<Block, SpaceTorch> SPACE_TORCH = BLOCKS.register("space_torch", id -> new SpaceTorch(BlockBehaviour.Properties.of().setId(blockKey(id))));
    public static final DeferredHolder<Block, Block> AZURIUM_BLOCK = registerBlock("azurium_block", id -> new Block(BlockBehaviour.Properties.of().setId(blockKey(id)).lightLevel((light) -> 6).noOcclusion()));
    public static final DeferredHolder<Block, Block> PYROLITH_BLOCK = registerBlock("pyrolith_block", id -> new Block(BlockBehaviour.Properties.of().setId(blockKey(id)).lightLevel((light) -> 6).noOcclusion()));
    public static final DeferredHolder<Block, Block> ELECTRYTE_BLOCK = registerBlock("electryte_block", id -> new Block(BlockBehaviour.Properties.of().setId(blockKey(id)).lightLevel((light) -> 6).noOcclusion()));
    public static final DeferredHolder<Block, Block> CHLOROSYNTH_BLOCK = registerBlock("chlorosynth_block", id -> new Block(BlockBehaviour.Properties.of().setId(blockKey(id)).lightLevel((light) -> 6).noOcclusion()));

    public static final DeferredHolder<Block, Block> VOIDSTONE = registerBlock("voidstone", RPBlocks::createStone);
    public static final DeferredHolder<Block, Block> MOSSY_VOIDSTONE = registerBlock("mossy_voidstone", RPBlocks::createStone);
    public static final DeferredHolder<Block, Block> COBBLED_VOIDSTONE = registerBlock("cobbled_voidstone", RPBlocks::createCobblestone);

    public static final DeferredHolder<Block, Block> VOIDSTONE_SPIKE_MIDDLE = registerBlock("voidstone_spike_middle", RPBlocks::createStoneSpikeM);
    public static final DeferredHolder<Block, Block> VOIDSTONE_SPIKE_TOP = registerBlock("voidstone_spike_top", RPBlocks::createStoneSpikeT);

    public static final DeferredHolder<Block, Block> DEEPSLATE_RUBY_ORE = registerBlock("deepslate_ruby_ore", RPBlocks::createDeepslateOreBlock);
    public static final DeferredHolder<Block, Block> DEEPSLATE_SAPPHIRE_ORE = registerBlock("deepslate_sapphire_ore", RPBlocks::createDeepslateOreBlock);
    public static final DeferredHolder<Block, Block> DEEPSLATE_TOPAZ_ORE = registerBlock("deepslate_topaz_ore", RPBlocks::createDeepslateOreBlock);
    public static final DeferredHolder<Block, Block> DEEPSLATE_OPAL_ORE = registerBlock("deepslate_opal_ore", RPBlocks::createDeepslateOreBlock);
    public static final DeferredHolder<Block, Block> DEEPSLATE_AMBER_ORE = registerBlock("deepslate_amber_ore", RPBlocks::createDeepslateOreBlock);
    public static final DeferredHolder<Block, Block> DEEPSLATE_ONYX_ORE = registerBlock("deepslate_onyx_ore", RPBlocks::createDeepslateOreBlock);
    public static final DeferredHolder<Block, Block> DEEPSLATE_PYRITE_ORE = registerBlock("deepslate_pyrite_ore", RPBlocks::createDeepslateOreBlock);
    public static final DeferredHolder<Block, Block> DEEPSLATE_THALLIUM_ORE = registerBlock("deepslate_thallium_ore", RPBlocks::createDeepslateOreBlock);
    public static final DeferredHolder<Block, Block> DEEPSLATE_QUARTZINE_ORE = registerBlock("deepslate_quartzine_ore", RPBlocks::createDeepslateOreBlock);

    public static final DeferredHolder<Block, Block> RUBY_ORE = registerBlock("ruby_ore", RPBlocks::createOreBlock);
    public static final DeferredHolder<Block, Block> SAPPHIRE_ORE = registerBlock("sapphire_ore", RPBlocks::createOreBlock);
    public static final DeferredHolder<Block, Block> TOPAZ_ORE = registerBlock("topaz_ore", RPBlocks::createOreBlock);
    public static final DeferredHolder<Block, Block> OPAL_ORE = registerBlock("opal_ore", RPBlocks::createOreBlock);
    public static final DeferredHolder<Block, Block> AMBER_ORE = registerBlock("amber_ore", RPBlocks::createOreBlock);
    public static final DeferredHolder<Block, Block> ONYX_ORE = registerBlock("onyx_ore", RPBlocks::createOreBlock);
    public static final DeferredHolder<Block, Block> PYRITE_ORE = registerBlock("pyrite_ore", RPBlocks::createOreBlock);
    public static final DeferredHolder<Block, Block> THALLIUM_ORE = registerBlock("thallium_ore", RPBlocks::createOreBlock);
    public static final DeferredHolder<Block, Block> QUARTZINE_ORE = registerBlock("quartzine_ore", RPBlocks::createOreBlock);

    public static Block createDeepslateOreBlock(Identifier id) {
        return new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_COAL_ORE).setId(blockKey(id)));
    }

    public static Block createOreBlock(Identifier id) {
        return new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COAL_ORE).setId(blockKey(id)));
    }

    public static Block createStone(Identifier id) {
        return new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).setId(blockKey(id)));
    }

    public static Block createCobblestone(Identifier id) {
        return new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE).setId(blockKey(id)));
    }

    public static Block createStoneSpikeM(Identifier id) {
        return new SpikeMiddle(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).setId(blockKey(id)));
    }

    public static Block createStoneSpikeT(Identifier id) {
        return new SpikeTop(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).setId(blockKey(id)));
    }

    public static DeferredHolder<Block, Block> registerBlock(String id, Function<Identifier, Block> blockFactory) {
        DeferredHolder<Block, Block> block = BLOCKS.register(id, blockFactory);
        RPItems.ITEMS.register(id, itemId -> new BlockItem(block.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, itemId))));
        return block;
    }
}
