package yaboichips.rogue_planets.common.items.crystals;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class Chlorosynth extends Item {

    public Chlorosynth(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        // MAKE A POTION EFFECT!
        player.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 60 * 20, 1));
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 30 * 20, 3));
        player.heal(20);

        level.addParticle(ParticleTypes.ELECTRIC_SPARK, player.getX(), player.getY(), player.getZ(), 0, 0, 0);
        level.playSound(player, player.blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS);
        player.getCooldowns().addCooldown(stack, 30 * 20);
        stack.shrink(1);
        return InteractionResult.CONSUME;
    }
}
