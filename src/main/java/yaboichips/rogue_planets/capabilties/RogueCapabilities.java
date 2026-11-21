package yaboichips.rogue_planets.capabilties;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import yaboichips.rogue_planets.capabilties.player.IPlayerData;

import static yaboichips.rogue_planets.RoguePlanets.MODID;

public class RogueCapabilities {
    public static final Capability<ArmorData.ArmorCapability> PADDING = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static final Capability<IPlayerData> PLAYER_DATA = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static final ResourceLocation PLAYER_DATA_LOCATION = ResourceLocation.fromNamespaceAndPath(MODID, "player_data");
    public static final ResourceLocation NEW_PLAYER_DATA_LOCATION = ResourceLocation.fromNamespaceAndPath(MODID, "new_player_data");

}
