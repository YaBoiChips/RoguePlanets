package yaboichips.rogue_planets.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import yaboichips.rogue_planets.core.RPEntities;
import yaboichips.rogue_planets.core.RPItems;

public class PortAPlatform extends ThrowableItemProjectile {
    int alive;

    public PortAPlatform(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
    }

    public PortAPlatform(Level p_37399_, LivingEntity p_37400_) {
        super(RPEntities.PORTAPLATFORM.get(), p_37400_, p_37399_);
    }

    public PortAPlatform(Level p_37394_, double p_37395_, double p_37396_, double p_37397_) {
        super(RPEntities.PORTAPLATFORM.get(), p_37395_, p_37396_, p_37397_, p_37394_);
    }


    @Override
    protected Item getDefaultItem() {
        return RPItems.PORTAPLATFORM.get();
    }

    @Override
    public void tick() {
        super.tick();
        alive++;
        System.out.println(alive);
        if (alive > 50) {
            createPlatform(this.blockPosition());
        }
    }

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            BlockPos platformY = new BlockPos((int) result.getLocation().x, (int) result.getLocation().y(), (int) result.getLocation().z());
            createPlatform(platformY);
        }
    }


    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            BlockPos platformY = new BlockPos((int) result.getLocation().x, (int) result.getLocation().y(), (int) result.getLocation().z());
            createPlatform(platformY);
        }
    }

    private void createPlatform(BlockPos platformY) {
        if (!this.level().isClientSide) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    this.level().setBlock(platformY.offset(dx, 0, dz), Blocks.SMOOTH_STONE.defaultBlockState(), Block.UPDATE_ALL);
                }
            }
            this.discard();
        }
    }
}
