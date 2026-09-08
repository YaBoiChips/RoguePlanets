package yaboichips.rogue_planets.core;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import yaboichips.rogue_planets.common.entities.PortAPlatform;
import yaboichips.rogue_planets.common.entities.canon.CanonEntity;
import yaboichips.rogue_planets.common.entities.monsters.Alien;
import yaboichips.rogue_planets.common.entities.monsters.Cyclops;
import yaboichips.rogue_planets.common.entities.workers.augmentor.Augmentor;
import yaboichips.rogue_planets.common.entities.workers.ceo.CEO;
import yaboichips.rogue_planets.common.entities.workers.forgemaster.ForgeMaster;
import yaboichips.rogue_planets.common.entities.workers.merchant.Merchant;

import static yaboichips.rogue_planets.RoguePlanets.MODID;

public class RPEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MODID);

    private static ResourceKey<EntityType<?>> key(String id) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MODID, id));
    }

    public static final DeferredHolder<EntityType<?>, EntityType<ForgeMaster>> FORGE_MASTER = ENTITIES.register("forge_master", () -> EntityType.Builder.of(ForgeMaster::new, MobCategory.MISC).sized(0.8f, 1.8f).build(key("forge_master")));
    public static final DeferredHolder<EntityType<?>, EntityType<Merchant>> RP_MERCHANT = ENTITIES.register("merchant", () -> EntityType.Builder.of(Merchant::new, MobCategory.MISC).sized(0.8f, 1.8f).build(key("merchant")));
    public static final DeferredHolder<EntityType<?>, EntityType<Augmentor>> AUGMENTOR = ENTITIES.register("augmentor", () -> EntityType.Builder.of(Augmentor::new, MobCategory.MISC).sized(0.8f, 1.8f).build(key("augmentor")));
    public static final DeferredHolder<EntityType<?>, EntityType<CEO>> CEO = ENTITIES.register("ceo", () -> EntityType.Builder.of(CEO::new, MobCategory.MISC).sized(0.8f, 1.8f).build(key("ceo")));
    public static final DeferredHolder<EntityType<?>, EntityType<Cyclops>> CYCLOPS = ENTITIES.register("cyclops", () -> EntityType.Builder.of(Cyclops::new, MobCategory.MONSTER).sized(0.8f, 1.8f).build(key("cyclops")));
    public static final DeferredHolder<EntityType<?>, EntityType<Alien>> ALIEN = ENTITIES.register("alien", () -> EntityType.Builder.of(Alien::new, MobCategory.MONSTER).sized(0.8f, 1.8f).build(key("alien")));
    public static final DeferredHolder<EntityType<?>, EntityType<CanonEntity>> CANON = ENTITIES.register("canon", () -> EntityType.Builder.of(CanonEntity::new, MobCategory.MISC).sized(1, 0.5f).build(key("canon")));
    public static final DeferredHolder<EntityType<?>, EntityType<PortAPlatform>> PORTAPLATFORM = ENTITIES.register("portaplatform", () -> EntityType.Builder.<PortAPlatform>of(PortAPlatform::new, MobCategory.MISC).sized(0.2f, 0.2f).build(key("portaplatform")));
}
