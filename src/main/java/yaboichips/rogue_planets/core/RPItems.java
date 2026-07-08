package yaboichips.rogue_planets.core;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yaboichips.rogue_planets.common.items.*;
import yaboichips.rogue_planets.common.items.augments.AugmentItem;
import yaboichips.rogue_planets.common.items.augments.AugmentType;
import yaboichips.rogue_planets.common.items.crystals.Azurium;
import yaboichips.rogue_planets.common.items.crystals.Electryte;
import yaboichips.rogue_planets.common.items.crystals.Chlorosynth;
import yaboichips.rogue_planets.common.items.crystals.Pyrolith;
import yaboichips.rogue_planets.common.items.tools.PlaneteerPickaxe;

import static yaboichips.rogue_planets.RoguePlanets.MODID;

public class RPItems {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);


//    public static final RegistryObject<Item> TELEPORTER_BLOCK_ITEM = ITEMS.register("teleporter_block", () -> new BlockItem(TELEPORTER_BLOCK.get(), new Item.Properties()));

    //Raw Materials
    public static final RegistryObject<Item> RUBY = ITEMS.register("ruby",() -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SAPPHIRE = ITEMS.register("sapphire", ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TOPAZ = ITEMS.register("topaz", ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> OPAL = ITEMS.register("opal", ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> AMBER = ITEMS.register("amber", ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ONYX = ITEMS.register("onyx", ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PYRITE = ITEMS.register("pyrite", ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> QUARTZINE = ITEMS.register("quartzine", ()-> new Item(new Item.Properties()));

    public static final RegistryObject<Item> THALLIUM_INGOT = ITEMS.register("thallium_ingot", ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> THALLIUM_DUST = ITEMS.register("thallium_dust", ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ENERGETIC_DUST = ITEMS.register("energetic_dust", ()-> new Item(new Item.Properties()));

    //Tools & Armor
    public static final RegistryObject<Item> PLANETEER_PICKAXE = ITEMS.register("planeteer_pickaxe", PlaneteerPickaxe::new);
    public static final RegistryObject<Item> PLANETEER_HELMET = ITEMS.register("planeteer_helmet",() -> new ExplorerSuit(ArmorMaterials.GOLD, ArmorItem.Type.HELMET, new Item.Properties(), 0));
    public static final RegistryObject<Item> PLANETEER_CHESTPLATE = ITEMS.register("planeteer_chestplate",() -> new ExplorerSuit(ArmorMaterials.GOLD, ArmorItem.Type.CHESTPLATE, new Item.Properties(), 1));
    public static final RegistryObject<Item> PLANETEER_LEGGINGS = ITEMS.register("planeteer_leggings",() -> new ExplorerSuit(ArmorMaterials.GOLD, ArmorItem.Type.LEGGINGS, new Item.Properties(), 2));
    public static final RegistryObject<Item> PLANETEER_BOOTS = ITEMS.register("planeteer_boots",() -> new ExplorerSuit(ArmorMaterials.GOLD, ArmorItem.Type.BOOTS, new Item.Properties(), 3));


    // Utility Items
    public static final RegistryObject<Item> ROPE = ITEMS.register("rope",() -> new RopeBlockItem(RPBlocks.ROPE.get(), new Item.Properties()));
    public static final RegistryObject<Item> SPACE_TORCH = ITEMS.register("space_torch",() -> new BlockItem(RPBlocks.SPACE_TORCH.get(), new Item.Properties()));
    public static final RegistryObject<Item> JERKY = ITEMS.register("jerky", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().meat().nutrition(5).saturationMod(4).build())));
    public static final RegistryObject<Item> MAGAZINE = ITEMS.register("ammo", () -> new Item(new Item.Properties()));


    public static final RegistryObject<Item> TEST_AUGMENT = ITEMS.register("test_augment", () -> new AugmentItem(AugmentType.HASTE));

    public static final RegistryObject<Item> CREDIT = ITEMS.register("credit", () -> new CreditItem(1));
    public static final RegistryObject<Item> HIGH_CREDIT = ITEMS.register("high_credit", () -> new CreditItem(9));
    public static final RegistryObject<Item> RARE_CREDIT = ITEMS.register("rare_credit", () -> new CreditItem(81));
    public static final RegistryObject<Item> LEGENDARY_CREDIT = ITEMS.register("legendary_credit", () -> new CreditItem(729));
    public static final RegistryObject<Item> PLANETEER_MANUEL = ITEMS.register("planeteer_manuel", PlaneteerManuel::new);

    //Crystals
    public static final RegistryObject<Item> PYROLITH = ITEMS.register("pyrolith", Pyrolith::new);
    public static final RegistryObject<Item> ELECTRYTE = ITEMS.register("electryte", Electryte::new);
    public static final RegistryObject<Item> CHLOROSYNTH = ITEMS.register("chlorosynth", Chlorosynth::new);
    public static final RegistryObject<Item> AZURIUM = ITEMS.register("azurium", Azurium::new);


    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("rp_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .title(Component.literal("Rouge Planets"))
            .icon(() -> RUBY.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                ITEMS.getEntries().forEach(item->output.accept(item.get()));
            }).build());
}