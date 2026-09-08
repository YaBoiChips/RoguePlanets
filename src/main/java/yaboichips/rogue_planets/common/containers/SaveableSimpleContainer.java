package yaboichips.rogue_planets.common.containers;

import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.Arrays;

public class SaveableSimpleContainer extends SimpleContainer {
    public SaveableSimpleContainer(int slots) {
        super(slots);
    }

    public SaveableSimpleContainer(Inventory inventory) {
        super(inventory.getContainerSize());
        inventory.getNonEquipmentItems().forEach(this::addItem);
    }

    public void addItems(ItemStack... items) {
        Arrays.stream(items).toList().forEach(this::addItem);
    }

    public void saveTo(ValueOutput output) {
        ContainerHelper.saveAllItems(output, this.getItems());
    }

    public void loadFrom(ValueInput input) {
        ContainerHelper.loadAllItems(input, this.getItems());
    }
}
