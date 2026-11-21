package yaboichips.rogue_planets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import commoble.infiniverse.api.InfiniverseAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import yaboichips.rogue_planets.capabilties.ArmorData;
import yaboichips.rogue_planets.capabilties.RogueCapabilities;
import yaboichips.rogue_planets.capabilties.player.ClientPlayerData;
import yaboichips.rogue_planets.capabilties.player.IPlayerData;
import yaboichips.rogue_planets.capabilties.player.PlayerDataProvider;
import yaboichips.rogue_planets.capabilties.player.PlayerDataUtils;
import yaboichips.rogue_planets.common.commands.PartyCommand;
import yaboichips.rogue_planets.common.entities.monsters.GenericMonster;
import yaboichips.rogue_planets.common.entities.workers.HumanMob;
import yaboichips.rogue_planets.common.items.augments.AugmentType;
import yaboichips.rogue_planets.common.items.augments.AugmentUtil;
import yaboichips.rogue_planets.common.world.DimensionSeeds;
import yaboichips.rogue_planets.core.RPEntities;
import yaboichips.rogue_planets.network.RoguePackets;
import yaboichips.rogue_planets.network.SendPlayerDataPacket;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static yaboichips.rogue_planets.core.RPBlockEntities.BLOCK_ENTITY_TYPES;
import static yaboichips.rogue_planets.core.RPBlocks.BLOCKS;
import static yaboichips.rogue_planets.core.RPEntities.ENTITIES;
import static yaboichips.rogue_planets.core.RPItems.CREATIVE_MODE_TABS;
import static yaboichips.rogue_planets.core.RPItems.ITEMS;
import static yaboichips.rogue_planets.core.RPMenus.MENUS;
import static yaboichips.rogue_planets.core.world.RPFeatures.FEATURES;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(RoguePlanets.MODID)
public class RoguePlanets {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "rogueplanets";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public static long currentTick = 0;
    private static final Map<Long, Runnable> scheduledTasks = new ConcurrentHashMap<>();
    public static ResourceKey<Level> MINER_DIMENSION = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(MODID, "miner_dimension"));

    /*
    TODO GET MOB SPAWNS WORKING
    TODO MAKE PLANET DARK
    TODO PLATFORM THROWABLE THING
     */

    public RoguePlanets() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ENTITIES.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        MENUS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        FEATURES.register(modEventBus);
        modEventBus.addListener(this::onCommonSetup);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(this::entityAttributes);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        LOGGER.info("Rogue Planets Registered");
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        RoguePackets.registerPackets();
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(ArmorData.class);
        event.register(IPlayerData.class);
    }

    public void entityAttributes(final EntityAttributeCreationEvent event) {
        event.put(RPEntities.FORGE_MASTER.get(), HumanMob.createAttributes().build());
        event.put(RPEntities.RP_MERCHANT.get(), HumanMob.createAttributes().build());
        event.put(RPEntities.AUGMENTOR.get(), HumanMob.createAttributes().build());
        event.put(RPEntities.CEO.get(), HumanMob.createAttributes().build());

        event.put(RPEntities.CYCLOPS.get(), GenericMonster.createAttributes().build());
        event.put(RPEntities.ALIEN.get(), GenericMonster.createAttributes().build());
    }

    public static void scheduleTask(long tick, Runnable task) {
        scheduledTasks.put(tick + RoguePlanets.currentTick, task);
    }

    public static void clearTask() {
        scheduledTasks.clear();
    }

    @SubscribeEvent
    public void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            player.reviveCaps();
            if (!event.getObject().getCapability(RogueCapabilities.PLAYER_DATA).isPresent()) {
                event.addCapability(RogueCapabilities.PLAYER_DATA_LOCATION, new PlayerDataProvider());
            }
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().level().isClientSide)
            return;
        if (event.getEntity() instanceof ServerPlayer player) {
            player.reviveCaps();
            RoguePackets.sendToPlayer(new SendPlayerDataPacket(PlayerDataUtils.getO2(player), PlayerDataUtils.getCredits(player)), player);
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        if (event.getOriginal().level().isClientSide)
            return;

        ResourceKey<Level> fromDimension = event.getOriginal().level().dimension();

        // Check if the dimension the player is leaving belongs to your mod
        if (fromDimension.location().getNamespace().equals(MODID)) {
            if (event.getEntity().getServer().getLevel(fromDimension).players().isEmpty()) {
                InfiniverseAPI.get().markDimensionForUnregistration(event.getEntity().getServer(), fromDimension);
                DimensionSeeds.remove(fromDimension);
            }
        }
        if (event.isWasDeath()) {
            event.getOriginal().reviveCaps();
            event.getOriginal().getCapability(RogueCapabilities.PLAYER_DATA).ifPresent(oldStore ->
                    event.getEntity().getCapability(RogueCapabilities.PLAYER_DATA).ifPresent(newStore ->
                            newStore.deserializeNBT(oldStore.serializeNBT())
                    )
            );
            event.getOriginal().invalidateCaps();
        }

    }


    @SubscribeEvent
    public void onPlayerAttack(AttackEntityEvent event) {
        event.getEntity().getCapability(RogueCapabilities.PLAYER_DATA).ifPresent(playerData -> {
            //replace with potion effect
//            if (playerData.isPyrolithActive()) {
//                event.getTarget().setSecondsOnFire(30);
//            }
//            if (playerData.isAzuriumActive()) {
//                ((LivingEntity) event.getTarget()).addEffect(new MobEffectInstance(MobEffects.LEVITATION, 200, 1));
//            }
        });
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        ResourceKey<Level> fromDimension = event.getFrom();
        // Check if the dimension the player is leaving belongs to your mod
        if (fromDimension.location().getNamespace().equals(MODID)) {
            if (event.getEntity().getServer().getLevel(fromDimension).players().isEmpty()) {
                InfiniverseAPI.get().markDimensionForUnregistration(event.getEntity().getServer(), fromDimension);
                DimensionSeeds.remove(fromDimension);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isClient()) return;
        ServerPlayer player = (ServerPlayer) event.player;
        RoguePackets.sendToPlayer(new SendPlayerDataPacket(PlayerDataUtils.getO2(player), PlayerDataUtils.getCredits(player)), player);
        if (player.serverLevel().dimension().location().getNamespace().equals("rogueplanets")) {
            int extraTanks = countExtraTanks(player);
            if (player.tickCount % (1 + extraTanks) == 0) {
                PlayerDataUtils.subO2(player, 1);
            }
            if (PlayerDataUtils.getO2(player) <= 0) {
                if (player.isAlive()) {
                    player.kill();
                }
            }
        }
    }

    private int countExtraTanks(ServerPlayer player) {
        int tanks = 0;
        if (AugmentUtil.getAugment(player.getItemBySlot(EquipmentSlot.HEAD)) == AugmentType.EXTRA_TANK) tanks++;
        if (AugmentUtil.getAugment(player.getItemBySlot(EquipmentSlot.CHEST)) == AugmentType.EXTRA_TANK) tanks++;
        return tanks;
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        PartyCommand.register(event.getDispatcher());
    }


    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            return;
        }
        currentTick++;
        Iterator<Map.Entry<Long, Runnable>> iterator = scheduledTasks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Runnable> entry = iterator.next();
            if (entry.getKey() <= currentTick) {
                try {
                    entry.getValue().run();
                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.info("ROGUE PLANET SERVER EVENT KILLED ITSELF, YELL AT CHIPS");
                }
                iterator.remove();
            }
        }
    }
}