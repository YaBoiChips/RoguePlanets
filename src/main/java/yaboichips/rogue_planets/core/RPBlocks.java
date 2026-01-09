package yaboichips.rogue_planets.core;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yaboichips.rogue_planets.common.blocks.RopeBlock;
import yaboichips.rogue_planets.common.blocks.SpikeMiddle;
import yaboichips.rogue_planets.common.blocks.SpikeTop;
import yaboichips.rogue_planets.common.items.SpaceTorch;

import java.util.function.Supplier;

import static yaboichips.rogue_planets.RoguePlanets.MODID;


public class RPBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final RegistryObject<Block> ROPE = BLOCKS.register("rope", () -> new RopeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).sound(SoundType.WOOL).noCollission()));
    public static final RegistryObject<Block> SPACE_TORCH = BLOCKS.register("space_torch", SpaceTorch::new);
    public static final RegistryObject<Block> AZURIUM_BLOCK = registerBlock("azurium_block", () -> new Block(BlockBehaviour.Properties.of().lightLevel((light) -> 6).noOcclusion()));
    public static final RegistryObject<Block> PYROLITH_BLOCK = registerBlock("pyrolith_block", () -> new Block(BlockBehaviour.Properties.of().lightLevel((light) -> 6).noOcclusion()));
    public static final RegistryObject<Block> ELECTRYTE_BLOCK = registerBlock("electryte_block", () -> new Block(BlockBehaviour.Properties.of().lightLevel((light) -> 6).noOcclusion()));
    public static final RegistryObject<Block> CHLOROSYNTH_BLOCK = registerBlock("chlorosynth_block", () -> new Block(BlockBehaviour.Properties.of().lightLevel((light) -> 6).noOcclusion()));



    public static final RegistryObject<Block> VOIDSTONE = registerBlock("voidstone", RPBlocks::createStone);
    public static final RegistryObject<Block> VOIDSTONE_SPIKE_MIDDLE = registerBlock("voidstone_spike_middle", RPBlocks::createStoneSpikeM);
    public static final RegistryObject<Block> VOIDSTONE_SPIKE_TOP = registerBlock("voidstone_spike_top", RPBlocks::createStoneSpikeT);


    //ALL DIS GONNA CHANGE BUCKOS!!!!!

    public static final RegistryObject<Block> DEEPSLATE_RUBY_ORE = registerBlock("deepslate_ruby_ore", RPBlocks::createDeepslateOreBlock);
    public static final RegistryObject<Block> DEEPSLATE_SAPPHIRE_ORE = registerBlock("deepslate_sapphire_ore", RPBlocks::createDeepslateOreBlock);
    public static final RegistryObject<Block> DEEPSLATE_TOPAZ_ORE = registerBlock("deepslate_topaz_ore", RPBlocks::createDeepslateOreBlock);
    public static final RegistryObject<Block> DEEPSLATE_OPAL_ORE = registerBlock("deepslate_opal_ore", RPBlocks::createDeepslateOreBlock);
    public static final RegistryObject<Block> DEEPSLATE_AMBER_ORE = registerBlock("deepslate_amber_ore", RPBlocks::createDeepslateOreBlock);
    public static final RegistryObject<Block> DEEPSLATE_ONYX_ORE = registerBlock("deepslate_onyx_ore", RPBlocks::createDeepslateOreBlock);
    public static final RegistryObject<Block> DEEPSLATE_PYRITE_ORE = registerBlock("deepslate_pyrite_ore", RPBlocks::createDeepslateOreBlock);
    public static final RegistryObject<Block> DEEPSLATE_THALLIUM_ORE = registerBlock("deepslate_thallium_ore", RPBlocks::createDeepslateOreBlock);
    public static final RegistryObject<Block> DEEPSLATE_QUARTZINE_ORE = registerBlock("deepslate_quartzine_ore", RPBlocks::createDeepslateOreBlock);

    public static final RegistryObject<Block> RUBY_ORE = registerBlock("ruby_ore", RPBlocks::createOreBlock);
    public static final RegistryObject<Block> SAPPHIRE_ORE = registerBlock("sapphire_ore", RPBlocks::createOreBlock);
    public static final RegistryObject<Block> TOPAZ_ORE = registerBlock("topaz_ore", RPBlocks::createOreBlock);
    public static final RegistryObject<Block> OPAL_ORE = registerBlock("opal_ore", RPBlocks::createOreBlock);
    public static final RegistryObject<Block> AMBER_ORE = registerBlock("amber_ore", RPBlocks::createOreBlock);
    public static final RegistryObject<Block> ONYX_ORE = registerBlock("onyx_ore", RPBlocks::createOreBlock);
    public static final RegistryObject<Block> PYRITE_ORE = registerBlock("pyrite_ore", RPBlocks::createOreBlock);
    public static final RegistryObject<Block> THALLIUM_ORE = registerBlock("thallium_ore", RPBlocks::createOreBlock);
    public static final RegistryObject<Block> QUARTZINE_ORE = registerBlock("quartzine_ore", RPBlocks::createOreBlock);

    public static Block createDeepslateOreBlock() {
        return new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_COAL_ORE));
    }

    public static Block createOreBlock() {
        return new Block(BlockBehaviour.Properties.copy(Blocks.COAL_ORE));
    }
    public static Block createStone() {
        return new Block(BlockBehaviour.Properties.copy(Blocks.STONE));
    }
    public static Block createStoneSpikeM() {
        return new SpikeMiddle(BlockBehaviour.Properties.copy(Blocks.STONE));
    }
    public static Block createStoneSpikeT() {
        return new SpikeTop(BlockBehaviour.Properties.copy(Blocks.STONE));
    }


    public static RegistryObject<Block> registerBlock(String id, Supplier<Block> blockSup){
        RegistryObject<Block> block = BLOCKS.register(id, blockSup);
        RPItems.ITEMS.register(id, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }
}
