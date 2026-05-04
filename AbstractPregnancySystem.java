package dev.dixmk.minepreggo.world.pregnancy;

import javax.annotation.Nonnull;

import dev.dixmk.minepreggo.client.resources.sounds.*;
import dev.dixmk.minepreggo.init.MinepreggoCapabilities;
import dev.dixmk.minepreggo.init.MinepreggoModMobEffects;
import dev.dixmk.minepreggo.init.MinepreggoModSounds;
import dev.dixmk.minepreggo.world.entity.LivingEntityHelper;
import dev.dixmk.minepreggo.world.entity.preggo.ITamablePregnantPreggoMob;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

public abstract class AbstractPregnancySystem<E extends LivingEntity> implements IPregnancySystem {

	protected E pregnantEntity;
	protected final RandomSource randomSource;
	
	private int stomachGrowlCooldown = 0;
	protected float stomachGrowlProb;
	private int stomachGrowlSound;
	
	private int pregnancyPainCooldown = 0;
	protected float pregnancyHurtProb = 0.1f;

	protected AbstractPregnancySystem(@Nonnull E pregnantEntity) {
		this.pregnantEntity = pregnantEntity;
		this.randomSource = pregnantEntity.getRandom();
	}
	
	protected abstract void evaluatePregnancySystem();
	
	protected abstract void evaluatePregnancyPains();
	
	protected abstract void evaluatePregnancyTimer();
	
	protected abstract void evaluateMiscarriage(ServerLevel serverLevel);
	
	protected abstract void evaluatePregnancySymptoms();
	
	public abstract boolean isMiscarriageActive();
	
	public abstract boolean hasPregnancyPain();
	
	public abstract boolean canAdvanceNextPregnancyPhase();
	
	public abstract boolean hasAllPregnancySymptoms();
	
	protected abstract void advanceToNextPregnancyPhase();
	
	protected abstract void initPostMiscarriage();
	
	protected abstract void initPostPartum();
	
	protected abstract boolean tryInitRandomPregnancyPain();
	
	protected abstract boolean tryInitPregnancySymptom();
	
	protected abstract boolean hasToGiveBirth();
	
	protected abstract boolean isInLabor();
	
	protected abstract void startLabor();
	
	protected abstract boolean isWaterBroken();
	
	protected abstract void breakWater();
	
	protected abstract void evaluateWaterBreaking(ServerLevel serverLevel);
	
	protected abstract void evaluateBirth(ServerLevel serverLevel);
	
	protected abstract void evaluatePregnancyNeeds();
	
	protected abstract void startMiscarriage();
	
	protected abstract void evaluatePregnancyHealing();
	
	public static void spawnParticulesForWaterBreaking(ServerLevel serverLevel, LivingEntity target) {	
		spawnParticulesForWaterBreaking(serverLevel, target, target.getBbHeight() * 0.35);
	}
	
	public static void spawnParticulesForWaterBreaking(ServerLevel serverLevel, LivingEntity target, double extraYOffset) {	
		for (ServerPlayer player : serverLevel.getServer().getPlayerList().getPlayers()) {
		    if (player.distanceToSqr(target) <= 256.0) { // 16 blocks
				serverLevel.sendParticles(
						player,
						ParticleTypes.FALLING_DRIPSTONE_WATER,
						true,
						target.getX(), (target.getY() + extraYOffset), target.getZ(),
						1,
						0, 0, 0,
						0.02);
		    }
		}
	}
	
	public static void spawnParticulesForMiscarriage(ServerLevel serverLevel, LivingEntity target) {	
		spawnParticulesForMiscarriage(serverLevel, target, target.getBbHeight() * 0.35);
	}
	
	public static void spawnParticulesForMiscarriage(ServerLevel serverLevel, LivingEntity target, double extraYOffset) {	
		for (ServerPlayer player : serverLevel.getServer().getPlayerList().getPlayers()) {
		    if (player.distanceToSqr(target) <= 256.0) { // 16 blocks
				serverLevel.sendParticles(
						player,
						ParticleTypes.FALLING_DRIPSTONE_LAVA,
						true,
						target.getX(), (target.getY() + extraYOffset), target.getZ(),
						1,
						0, 0, 0,
						0.02);
		    }
		}
	}
	
	protected void playStomachGrowls() {
		++this.stomachGrowlCooldown;
		if (this.stomachGrowlCooldown >= 20) {
			this.stomachGrowlCooldown = 0;
			this.stomachGrowlSound = -1;
			SoundManager soundManager = Minecraft.getInstance().getSoundManager();
			int foodLevel;
			if (this.pregnantEntity instanceof ServerPlayer player) {
				foodLevel = player.getFoodData().getFoodLevel();
				this.stomachGrowlProb = (0.125f - (foodLevel / 100f) / 2f);
				if (this.randomSource.nextFloat() < this.stomachGrowlProb) {
					player.getCapability(MinepreggoCapabilities.PLAYER_DATA).ifPresent(cap -> {
						cap.getFemaleData().ifPresent(femaleData -> {
							var pregnancySystem = femaleData.getPregnancyData();
							final var pregnancyHealth = pregnancySystem.getPregnancyHealth();
							if (pregnancyHealth <= 40
									|| PregnancyPain.isLaborPain(pregnancySystem.getPregnancyPain())) {
								this.stomachGrowlSound = 0;
							}
						});
					});

					if (player.hasEffect(MinepreggoModMobEffects.MORNING_SICKNESS.get())) {
						this.stomachGrowlSound = 5;
					} else if (this.stomachGrowlSound != 0) {
						if (player.getFoodData().getSaturationLevel() > 0) {
							this.stomachGrowlSound = 1;
						} else if (foodLevel <= 6) {
							this.stomachGrowlSound = 2;
						} else if (foodLevel >= 14) {
							this.stomachGrowlSound = 3;
						} else {
							this.stomachGrowlSound = 4;
						}
					}

					soundManager.play(getRandomStomachGrowls(this.pregnantEntity, this.stomachGrowlSound));
				}
			} else if (this.pregnantEntity instanceof ITamablePregnantPreggoMob handler) {
				foodLevel = handler.getTamableData().getFullness();
				this.stomachGrowlProb = (0.125f - (foodLevel / 100f) / 2f);
				if (this.randomSource.nextFloat() < this.stomachGrowlProb) {
					var pregnancySystem = handler.getPregnancyData();
					var pregnancyHealth = pregnancySystem.getPregnancyHealth();
					if (pregnancyHealth <= 40
							|| PregnancyPain.isLaborPain(pregnancySystem.getPregnancyPain())) {
						this.stomachGrowlSound = 0;
					}

					if (this.pregnantEntity.hasEffect(MinepreggoModMobEffects.MORNING_SICKNESS.get())) {
						this.stomachGrowlSound = 5;
					} else if (this.stomachGrowlSound != 0) {
						if (foodLevel >= 20) {
							this.stomachGrowlSound = 1;
						} else if (foodLevel <= 6) {
							this.stomachGrowlSound = 2;
						} else if (foodLevel >= 14) {
							this.stomachGrowlSound = 3;
						} else {
							this.stomachGrowlSound = 4;
						}
					}
					soundManager.play(getRandomStomachGrowls(this.pregnantEntity, this.stomachGrowlSound));
				}
			}
		}
	}

	protected StomachSoundInstance getRandomStomachGrowls(E entity, int value) {
		return switch (value) {
			case 0 ->
				new StomachAchingSoundInstance(entity);
			case 1 ->
				new StomachSatedSoundInstance(entity);
			case 2 ->
				new StomachEmptySoundInstance(entity);
			case 3 ->
				new StomachFullSoundInstance(entity);
			case 4 ->
				new StomachDigestingSoundInstance(entity);
			default ->
				new StomachUpsetSoundInstance(entity);
		};
	}

	protected void tryHurt() {
		if (pregnantEntity.getHealth() > 1.5f) {
			PregnancySystemHelper.applyDamageByPregnancyPain(pregnantEntity, 1);
		}
	}
	
	protected void tryHurtByCooldown(int coolDownTicks) {
		++pregnancyPainCooldown;
		if (this.pregnancyPainCooldown >= coolDownTicks && this.pregnantEntity.getHealth() >= 1.5f) {
			this.pregnancyPainCooldown = 0;
			if (this.randomSource.nextFloat() < this.pregnancyHurtProb) {
				PregnancySystemHelper.applyDamageByPregnancyPain(this.pregnantEntity, 1);
			}
		}
	}
	
	protected void tryHurtByCooldown() {
		tryHurtByCooldown(60);
	}
	
	protected boolean isMovingRidingSaddledHorse() {
		return this.pregnantEntity.isPassenger()
		&& this.pregnantEntity.getVehicle() instanceof AbstractHorse horse
		&& horse.isSaddled()
		&& (pregnantEntity.xxa != 0 || pregnantEntity.zza != 0);
	}
}