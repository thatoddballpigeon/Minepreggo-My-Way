package dev.dixmk.minepreggo.world.effect;

import javax.annotation.Nullable;

import dev.dixmk.minepreggo.init.MinepreggoCapabilities;
import dev.dixmk.minepreggo.world.entity.LivingEntityHelper;
import dev.dixmk.minepreggo.world.entity.preggo.ITamablePregnantPreggoMob;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class PregnancyHarming extends InstantenousMobEffect {
	public PregnancyHarming() {
		super(MobEffectCategory.HARMFUL, -3407821);
	}
	
    @Override
    public void applyEffectTick(LivingEntity target, int amplifier) {
        applyInstantenousEffect(null, null, target, amplifier, 1.0D);
    }
	
	@Override
	public void applyInstantenousEffect(@Nullable Entity p_19462_, @Nullable Entity p_19463_, LivingEntity p_19464_, int p_19465_, double p_19466_) { 
		if (p_19464_.level().isClientSide) {
			return;
		}

		final int pregnancyHealth;
		if (p_19464_ instanceof ITamablePregnantPreggoMob p) {
			p.getPregnancyData().reducePregnancyHealth(30);
			p_19464_.hurt(new DamageSource(p_19464_.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC)), 1);

			if (p.getPregnancyData().getPregnancyHealth() <= 40) {
				LivingEntityHelper.playStomachGrowlSound(p_19464_, p_19464_.getId(), 5);
			} else {
				LivingEntityHelper.playStomachGrowlSound(p_19464_, p_19464_.getId(), 0);
			}
		} else if (p_19464_ instanceof ServerPlayer player) {
			player.getCapability(MinepreggoCapabilities.PLAYER_DATA).ifPresent(cap -> 			
				cap.getFemaleData().ifPresent(femaleData -> {
					if (femaleData.isPregnant() && femaleData.isPregnancyDataInitialized()) {
						femaleData.getPregnancyData().reducePregnancyHealth(30);
						p_19464_.hurt(new DamageSource(p_19464_.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC)), 1);

						if (femaleData.getPregnancyData().getPregnancyHealth() <= 40) {
							LivingEntityHelper.playStomachGrowlSound(p_19464_, p_19464_.getId(), 5);
						} else {
							LivingEntityHelper.playStomachGrowlSound(p_19464_, p_19464_.getId(), 0);
						}
					}	
				})
			);
		}
	}
}
