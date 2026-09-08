package yaboichips.rogue_planets;

import com.mojang.logging.LogUtils;
import net.commoble.infiniverse.api.InfiniverseAPI;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.slf4j.Logger;
import yaboichips.rogue_planets.common.commands.PartyCommand;
import yaboichips.rogue_planets.common.entities.monsters.GenericMonster;
import yaboichips.rogue_planets.common.entities.workers.HumanMob;
import yaboichips.rogue_planets.core.RPEntities;
import yaboichips.rogue_planets.data.PlayerDataUtils;
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
import static yaboichips.rogue_planets.data.RPAttachments.ATTACHMENT_TYPES;
import static yaboichips.rogue_planets.data.RPDataComponents.DATA_COMPONENTS;

@Mod(RoguePlanets.MODID)
public class RoguePlanets {

    public static final String MODID = "rogueplanets";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static long currentTick = 0;
    private static final Map<Long, Runnable> scheduledTasks = new ConcurrentHashMap<>();
    public static final ResourceKey<Level> MINER_DIMENSION = ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath(MODID, "miner_dimension"));

    public RoguePlanets(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.register(this);
        ENTITIES.register(modEventBus);
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        MENUS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        FEATURES.register(modEventBus);
        ATTACHMENT_TYPES.register(modEventBus);
        DATA_COMPONENTS.register(modEventBus);
        modEventBus.addListener(this::entityAttributes);
        modEventBus.addListener(RoguePackets::registerPackets);
        LOGGER.info("Rogue Planets Registered");
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
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().level().isClientSide())
            return;
        if (event.getEntity() instanceof ServerPlayer player) {
            RoguePackets.sendToPlayer(new SendPlayerDataPacket(PlayerDataUtils.getO2(player), PlayerDataUtils.getCredits(player)), player);
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        if (event.getOriginal().level().isClientSide())
            return;

        ResourceKey<Level> fromDimension = event.getOriginal().level().dimension();

        // Check if the dimension the player is leaving belongs to this mod
        if (fromDimension.identifier().getNamespace().equals(MODID) && event.getEntity() instanceof ServerPlayer serverPlayer) {
            MinecraftServer server = serverPlayer.level().getServer();
            if (server.getLevel(fromDimension).players().isEmpty()) {
                InfiniverseAPI.get().markDimensionForUnregistration(server, fromDimension);
            }
        }
        // Player data attachments copy on death automatically via .copyOnDeath()
    }

    @SubscribeEvent
    public void onPlayerAttack(AttackEntityEvent event) {
        // replace with potion effect (see crystal items)
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        ResourceKey<Level> fromDimension = event.getFrom();
        if (fromDimension.identifier().getNamespace().equals(MODID) && event.getEntity() instanceof ServerPlayer serverPlayer) {
            MinecraftServer server = serverPlayer.level().getServer();
            if (server.getLevel(fromDimension).players().isEmpty()) {
                InfiniverseAPI.get().markDimensionForUnregistration(server, fromDimension);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide()) return;
        ServerPlayer player = (ServerPlayer) event.getEntity();
        RoguePackets.sendToPlayer(new SendPlayerDataPacket(PlayerDataUtils.getO2(player), PlayerDataUtils.getCredits(player)), player);
        if (player.level().dimension().identifier().getNamespace().equals(MODID)) {
            PlayerDataUtils.subO2(player, 1);
            if (PlayerDataUtils.getO2(player) <= 0) {
                if (player.isAlive()) {
                    player.kill(player.level());
                }
            }
        }
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        PartyCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        currentTick++;
        Iterator<Map.Entry<Long, Runnable>> iterator = scheduledTasks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Runnable> entry = iterator.next();
            if (entry.getKey() <= currentTick) {
                try {
                    entry.getValue().run();
                } catch (Exception e) {
                    LOGGER.error("ROGUE PLANET SERVER EVENT KILLED ITSELF, YELL AT CHIPS", e);
                }
                iterator.remove();
            }
        }
    }
}
