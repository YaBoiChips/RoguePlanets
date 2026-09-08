package yaboichips.rogue_planets.core;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import yaboichips.rogue_planets.common.items.CreditItem;
import yaboichips.rogue_planets.common.items.ExplorerSuit;
import yaboichips.rogue_planets.common.items.PlaneteerManuel;
import yaboichips.rogue_planets.common.items.RopeBlockItem;
import yaboichips.rogue_planets.common.items.augments.AugmentItem;
import yaboichips.rogue_planets.common.items.augments.AugmentType;
import yaboichips.rogue_planets.common.items.crystals.Azurium;
import yaboichips.rogue_planets.common.items.crystals.Chlorosynth;
import yaboichips.rogue_planets.common.items.crystals.Electryte;
import yaboichips.rogue_planets.common.items.crystals.Pyrolith;
import yaboichips.rogue_planets.common.items.tools.PlaneteerPickaxe;
import yaboichips.rogue_planets.common.items.tools.PortAPlatformItem;

import static yaboichips.rogue_planets.RoguePlanets.MODID;

public class RPItems {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, MODID);

    private static Item.Properties props(Identifier id) {
        return new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id));
    }

    // Raw Materials
    public static final DeferredHolder<Item, Item> RUBY = ITEMS.register("ruby", id -> new Item(props(id)));
    public static final DeferredHolder<Item, Item> SAPPHIRE = ITEMS.register("sapphire", id -> new Item(props(id)));
    public static final DeferredHolder<Item, Item> TOPAZ = ITEMS.register("topaz", id -> new Item(props(id)));
    public static final DeferredHolder<Item, Item> OPAL = ITEMS.register("opal", id -> new Item(props(id)));
    public static final DeferredHolder<Item, Item> AMBER = ITEMS.register("amber", id -> new Item(props(id)));
    public static final DeferredHolder<Item, Item> ONYX = ITEMS.register("onyx", id -> new Item(props(id)));
    public static final DeferredHolder<Item, Item> PYRITE = ITEMS.register("pyrite", id -> new Item(props(id)));
    public static final DeferredHolder<Item, Item> QUARTZINE = ITEMS.register("quartzine", id -> new Item(props(id)));

    public static final DeferredHolder<Item, Item> THALLIUM_INGOT = ITEMS.register("thallium_ingot", id -> new Item(props(id)));
    public static final DeferredHolder<Item, Item> THALLIUM_DUST = ITEMS.register("thallium_dust", id -> new Item(props(id)));
    public static final DeferredHolder<Item, Item> ENERGETIC_DUST = ITEMS.register("energetic_dust", id -> new Item(props(id)));

    // Tools & Armor
    public static final DeferredHolder<Item, PlaneteerPickaxe> PLANETEER_PICKAXE = ITEMS.register("planeteer_pickaxe", id -> new PlaneteerPickaxe(props(id)));
    public static final DeferredHolder<Item, ExplorerSuit> PLANETEER_HELMET = ITEMS.register("planeteer_helmet", id -> new ExplorerSuit(props(id).humanoidArmor(ArmorMaterials.GOLD, ArmorType.HELMET)));
    public static final DeferredHolder<Item, ExplorerSuit> PLANETEER_CHESTPLATE = ITEMS.register("planeteer_chestplate", id -> new ExplorerSuit(props(id).humanoidArmor(ArmorMaterials.GOLD, ArmorType.CHESTPLATE)));
    public static final DeferredHolder<Item, ExplorerSuit> PLANETEER_LEGGINGS = ITEMS.register("planeteer_leggings", id -> new ExplorerSuit(props(id).humanoidArmor(ArmorMaterials.GOLD, ArmorType.LEGGINGS)));
    public static final DeferredHolder<Item, ExplorerSuit> PLANETEER_BOOTS = ITEMS.register("planeteer_boots", id -> new ExplorerSuit(props(id).humanoidArmor(ArmorMaterials.GOLD, ArmorType.BOOTS)));

    // Utility Items
    public static final DeferredHolder<Item, RopeBlockItem> ROPE = ITEMS.register("rope", id -> new RopeBlockItem(RPBlocks.ROPE.get(), props(id)));
    public static final DeferredHolder<Item, BlockItem> SPACE_TORCH = ITEMS.register("space_torch", id -> new BlockItem(RPBlocks.SPACE_TORCH.get(), props(id)));
    public static final DeferredHolder<Item, Item> JERKY = ITEMS.register("jerky", id -> new Item(props(id).food(new FoodProperties.Builder().nutrition(5).saturationModifier(4).build())));
    public static final DeferredHolder<Item, Item> MAGAZINE = ITEMS.register("ammo", id -> new Item(props(id)));

    public static final DeferredHolder<Item, PortAPlatformItem> PORTAPLATFORM = ITEMS.register("portaplatform", id -> new PortAPlatformItem(props(id)));

    public static final DeferredHolder<Item, AugmentItem> TEST_AUGMENT = ITEMS.register("test_augment", id -> new AugmentItem(props(id), AugmentType.HASTE));

    public static final DeferredHolder<Item, CreditItem> CREDIT = ITEMS.register("credit", id -> new CreditItem(props(id), 1));
    public static final DeferredHolder<Item, CreditItem> HIGH_CREDIT = ITEMS.register("high_credit", id -> new CreditItem(props(id), 9));
    public static final DeferredHolder<Item, CreditItem> RARE_CREDIT = ITEMS.register("rare_credit", id -> new CreditItem(props(id), 81));
    public static final DeferredHolder<Item, CreditItem> LEGENDARY_CREDIT = ITEMS.register("legendary_credit", id -> new CreditItem(props(id), 729));
    public static final DeferredHolder<Item, PlaneteerManuel> PLANETEER_MANUEL = ITEMS.register("planeteer_manuel", id -> new PlaneteerManuel(props(id)));

    // Crystals
    public static final DeferredHolder<Item, Pyrolith> PYROLITH = ITEMS.register("pyrolith", id -> new Pyrolith(props(id)));
    public static final DeferredHolder<Item, Electryte> ELECTRYTE = ITEMS.register("electryte", id -> new Electryte(props(id)));
    public static final DeferredHolder<Item, Chlorosynth> CHLOROSYNTH = ITEMS.register("chlorosynth", id -> new Chlorosynth(props(id)));
    public static final DeferredHolder<Item, Azurium> AZURIUM = ITEMS.register("azurium", id -> new Azurium(props(id)));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("rp_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .title(Component.literal("Rouge Planets"))
            .icon(() -> RUBY.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                ITEMS.getEntries().forEach(item -> output.accept(item.get()));
            }).build());
}
