package yaboichips.rogue_planets.core;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import yaboichips.rogue_planets.common.entities.workers.augmentor.AugmentorMenu;
import yaboichips.rogue_planets.common.entities.workers.ceo.CEOMenu;
import yaboichips.rogue_planets.common.entities.workers.forgemaster.ForgeMasterMenu;
import yaboichips.rogue_planets.common.entities.workers.merchant.MerchantMenu;

import static yaboichips.rogue_planets.RoguePlanets.MODID;

public class RPMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<ForgeMasterMenu>> FORGE_MASTER_MENU = MENUS.register("forge_master_menu", () -> IMenuTypeExtension.create(ForgeMasterMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<MerchantMenu>> MERCHANT_MENU = MENUS.register("rp_merchant_menu", () -> IMenuTypeExtension.create(MerchantMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<AugmentorMenu>> AUGMENTOR_MENU = MENUS.register("augmentor_menu", () -> IMenuTypeExtension.create(AugmentorMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<CEOMenu>> CEO_MENU = MENUS.register("ceo_menu", () -> IMenuTypeExtension.create(CEOMenu::new));
}
