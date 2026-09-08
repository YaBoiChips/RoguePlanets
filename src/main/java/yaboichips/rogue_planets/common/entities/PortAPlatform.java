package yaboichips.rogue_planets.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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

    public PortAPlatform(Level level, LivingEntity shooter, ItemStack stack) {
        super(RPEntities.PORTAPLATFORM.get(), shooter, level, stack);
    }

    public PortAPlatform(Level level, double x, double y, double z, ItemStack stack) {
        super(RPEntities.PORTAPLATFORM.get(), x, y, z, level, stack);
    }

    @Override
    protected Item getDefaultItem() {
        return RPItems.PORTAPLATFORM.get();
    }

    @Override
    public void tick() {
        super.tick();
        alive++;
        if (alive > 50) {
            createPlatform(this.blockPosition());
        }
    }

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide()) {
            BlockPos platformY = new BlockPos((int) result.getLocation().x, (int) result.getLocation().y(), (int) result.getLocation().z());
            createPlatform(platformY);
        }
    }

    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide()) {
            BlockPos platformY = new BlockPos((int) result.getLocation().x, (int) result.getLocation().y(), (int) result.getLocation().z());
            createPlatform(platformY);
        }
    }

    private void createPlatform(BlockPos platformY) {
        if (!this.level().isClientSide()) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    this.level().setBlock(platformY.offset(dx, 0, dz), Blocks.SMOOTH_STONE.defaultBlockState(), Block.UPDATE_ALL);
                }
            }
            this.discard();
        }
    }
}
