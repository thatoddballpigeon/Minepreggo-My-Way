package dev.dixmk.minepreggo.init;

import dev.dixmk.minepreggo.MinepreggoMod;
import dev.dixmk.minepreggo.world.effect.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MinepreggoModMobEffects {
	
	private MinepreggoModMobEffects() {}
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MinepreggoMod.MODID);

	public static final RegistryObject<MobEffect> PREGNANCY_RESISTANCE = REGISTRY.register("pregnancy_resistance", PregnancyResistance::new);
	
	public static final RegistryObject<MobEffect> IMPREGNANTION = REGISTRY.register("impregnantion", Impregnantion::new);
	public static final RegistryObject<MobEffect> ZOMBIE_IMPREGNATION = REGISTRY.register("zombie_impregnation", ZombieImpregnation::new);
	public static final RegistryObject<MobEffect> CREEPER_IMPREGNATION = REGISTRY.register("creeper_impregnation", CreeperImpregnation::new);
	public static final RegistryObject<MobEffect> ENDER_IMPREGNATION = REGISTRY.register("ender_impregnation", EnderImpregnation::new);
	public static final RegistryObject<MobEffect> HUMANOID_CREEPER_IMPREGNATION = REGISTRY.register("humanoid_creeper_impregnation", HumanoidCreeperImpregnation::new);
	public static final RegistryObject<MobEffect> HUMANOID_ENDER_IMPREGNATION = REGISTRY.register("humanoid_ender_impregnation", HumanoidEnderImpregnation::new);
	public static final RegistryObject<MobEffect> VILLAGER_IMPREGNATION = REGISTRY.register("villager_impregnation", VillagerImpregnation::new);

	public static final RegistryObject<MobEffect> PREGNANCY_HEALING = REGISTRY.register("pregnancy_healing", PregnancyHealing::new);
	public static final RegistryObject<MobEffect> PREGNANCY_HARMING = REGISTRY.register("pregnancy_harming", PregnancyHarming::new);

	public static final RegistryObject<MobEffect> PREGNANCY_DELAY = REGISTRY.register("pregnancy_delay", PregnancyDelay::new);
	public static final RegistryObject<MobEffect> PREGNANCY_ACCELERATION = REGISTRY.register("pregnancy_acceleration", PregnancyAcceleration::new);
	public static final RegistryObject<MobEffect> BABY_DUPLICATION = REGISTRY.register("baby_duplication", BabyDuplication::new);
	
	public static final RegistryObject<MobEffect> FULL_OF_ZOMBIES = REGISTRY.register("full_of_zombies", FullOfZombies::new);
	public static final RegistryObject<MobEffect> FULL_OF_CREEPERS = REGISTRY.register("full_of_creepers", FullOfCreepers::new);
	public static final RegistryObject<MobEffect> FULL_OF_ENDERS = REGISTRY.register("full_of_enders", FullOfEnders::new);

	public static final RegistryObject<MobEffect> PREGNANCY_P0 = REGISTRY.register("pregnancy_p0", PregnancyP0::new);
	public static final RegistryObject<MobEffect> PREGNANCY_P1 = REGISTRY.register("pregnancy_p1", PregnancyP1::new);
	public static final RegistryObject<MobEffect> PREGNANCY_P2 = REGISTRY.register("pregnancy_p2", PregnancyP2::new);
	public static final RegistryObject<MobEffect> PREGNANCY_P3 = REGISTRY.register("pregnancy_p3", PregnancyP3::new);
	public static final RegistryObject<MobEffect> PREGNANCY_P4 = REGISTRY.register("pregnancy_p4", PregnancyP4::new);
	public static final RegistryObject<MobEffect> PREGNANCY_P5 = REGISTRY.register("pregnancy_p5", PregnancyP5::new);
	public static final RegistryObject<MobEffect> PREGNANCY_P6 = REGISTRY.register("pregnancy_p6", PregnancyP6::new);
	public static final RegistryObject<MobEffect> PREGNANCY_P7 = REGISTRY.register("pregnancy_p7", PregnancyP7::new);
	public static final RegistryObject<MobEffect> PREGNANCY_P8 = REGISTRY.register("pregnancy_p8", PregnancyP8::new);
		
	public static final RegistryObject<MobEffect> BELLY_RUBS = REGISTRY.register("belly_rubs", BellyRubs::new);
	public static final RegistryObject<MobEffect> BIRTH = REGISTRY.register("birth", Birth::new);
	public static final RegistryObject<MobEffect> CONTRACTION = REGISTRY.register("contraction", Contraction::new);
	public static final RegistryObject<MobEffect> CRAVING = REGISTRY.register("craving", Craving::new);
	public static final RegistryObject<MobEffect> DEPRESSION = REGISTRY.register("depression", Depression::new);

	public static final RegistryObject<MobEffect> ETERNAL_PREGNANCY = REGISTRY.register("eternal_pregnancy", () -> new MobEffect(MobEffectCategory.HARMFUL, -44381978) {});
	public static final RegistryObject<MobEffect> FERTILITY = REGISTRY.register("fertility", Fertility::new);
	public static final RegistryObject<MobEffect> FERTILE = REGISTRY.register("fertile", () -> new MobEffect(MobEffectCategory.BENEFICIAL, -10027213) {});
	public static final RegistryObject<MobEffect> FETAL_MOVEMENT = REGISTRY.register("fetal_movement", FetalMovement::new);
	public static final RegistryObject<MobEffect> WATER_BREAKING = REGISTRY.register("water_breaking", WaterBreaking::new);

	public static final RegistryObject<MobEffect> HORNY = REGISTRY.register("horny", Horny::new);
	public static final RegistryObject<MobEffect> LACTATION = REGISTRY.register("lactation", Lactation::new);
	public static final RegistryObject<MobEffect> MATERNITY = REGISTRY.register("maternity", Maternity::new);
	public static final RegistryObject<MobEffect> MISCARRIAGE = REGISTRY.register("miscarriage", Miscarriage::new);
	public static final RegistryObject<MobEffect> MORNING_SICKNESS = REGISTRY.register("morning_sickness", MorningSickness::new);
	public static final RegistryObject<MobEffect> PREBIRTH = REGISTRY.register("prebirth", PreBirth::new);

	public static final RegistryObject<MobEffect> BELLY_LUBRICATION = REGISTRY.register("belly_lubrication", () -> new MobEffect(MobEffectCategory.BENEFICIAL, -10027213) {});
	public static final RegistryObject<MobEffect> ZERO_GRAVITY_BELLY = REGISTRY.register("zero_gravity_belly", ZeroGravityBelly::new);

	public static final RegistryObject<MobEffect> METABOLISM_CONTROL = REGISTRY.register("metabolism_control", MetabolismControl::new);
	public static final RegistryObject<MobEffect> INFERTILITY = REGISTRY.register("infertility", Infertility::new);
	public static final RegistryObject<MobEffect> POISON_IMMUNITY = REGISTRY.register("poison_immunity", () -> new MobEffect(MobEffectCategory.BENEFICIAL, -10027213) {});
	public static final RegistryObject<MobEffect> WIHER_IMMUNITY = REGISTRY.register("wither_immunity", () -> new MobEffect(MobEffectCategory.BENEFICIAL, -13210) {});
	public static final RegistryObject<MobEffect> ENDER_DRAGON_IMPREGNATION = REGISTRY.register("ender_dragon_impregnation", EnderDragonImpregnation::new);
	public static final RegistryObject<MobEffect> ENDER_DRAGON_PREGNANCY = REGISTRY.register("ender_dragon_pregnancy", EnderDragonPregnancy::new);	

	public static final RegistryObject<MobEffect> ENDER_DRAGON_RECOGNITION = REGISTRY.register("ender_dragon_recognition", () -> new MobEffect(MobEffectCategory.BENEFICIAL, -3407668) {});	
	public static final RegistryObject<MobEffect> ENDER_ESSENCE = REGISTRY.register("ender_essence", () -> new MobEffect(MobEffectCategory.BENEFICIAL, -13434829) {});	
	public static final RegistryObject<MobEffect> ENDER_DRAGON_ESSENCE = REGISTRY.register("ender_dragon_essence", () -> new MobEffect(MobEffectCategory.BENEFICIAL, -3407668) {});	
	
	public static boolean hasImpregnationEffect(LivingEntity entity) {
		return entity.hasEffect(IMPREGNANTION.get())
				|| entity.hasEffect(ZOMBIE_IMPREGNATION.get())
				|| entity.hasEffect(CREEPER_IMPREGNATION.get()) 
				|| entity.hasEffect(ENDER_IMPREGNATION.get())
				|| entity.hasEffect(HUMANOID_CREEPER_IMPREGNATION.get())
				|| entity.hasEffect(HUMANOID_ENDER_IMPREGNATION.get())
				|| entity.hasEffect(VILLAGER_IMPREGNATION.get())
				|| entity.hasEffect(ENDER_DRAGON_IMPREGNATION.get());
	}
}																																																			
