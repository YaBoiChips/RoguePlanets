package yaboichips.rogue_planets.capabilties.player;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import yaboichips.rogue_planets.common.containers.PlanetArmorContainer;
import yaboichips.rogue_planets.common.containers.PlanetInventoryContainer;
import yaboichips.rogue_planets.common.containers.SaveableSimpleContainer;

public interface IPlayerData extends INBTSerializable<CompoundTag> {

    PlanetInventoryContainer getPlanetContainer();

    PlanetArmorContainer getArmorContainer();


    void setArmorContainer(PlanetArmorContainer armorContainer);


    void setPlanetContainer(PlanetInventoryContainer container);

    SaveableSimpleContainer getSavedInventory();

    void setSavedInventory(SaveableSimpleContainer playerInventory);

    boolean getIsInitiated();

    void setIsInitiated(boolean isInitiated);

    int getCredits();

    void setCredits(int credits);


    void addCredits(int credits);


    void subtractCredits(int credits);


    int getO2();


    void setO2(int o2);


    void addO2(int o2);


    void subO2(int o2);
}
