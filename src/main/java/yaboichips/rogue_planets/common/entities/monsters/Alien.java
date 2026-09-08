package yaboichips.rogue_planets.common.entities.monsters;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import static yaboichips.rogue_planets.RoguePlanets.MODID;

public class Alien extends GenericMonster {
    public Alien(EntityType<? extends Monster> monster, Level level) {
        super(monster, level);
    }

    @Override
    public Identifier getTextureLocation() {
        return Identifier.fromNamespaceAndPath(MODID, "textures/entity/alien.png");
    }
}
