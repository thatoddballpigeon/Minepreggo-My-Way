package dev.dixmk.minepreggo.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;

import dev.dixmk.minepreggo.MinepreggoMod;
import dev.dixmk.minepreggo.utils.MinepreggoHelper;

public class MinepreggoModSounds {
	
	private MinepreggoModSounds() {}

	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MinepreggoMod.MODID);

	public static final RegistryObject<SoundEvent> BELLY_TOUCH = REGISTRY.register("belly_touch", () -> SoundEvent.createVariableRangeEvent(MinepreggoHelper.fromNamespaceAndPath(MinepreggoMod.MODID, "belly_touch")));
	public static final RegistryObject<SoundEvent> PREGNANT_PREGGO_MOB_DEATH = REGISTRY.register("pregnant_preggo_mob_death", () -> SoundEvent.createVariableRangeEvent(MinepreggoHelper.fromNamespaceAndPath(MinepreggoMod.MODID, "pregnant_preggo_mob_death")));
	
	public static final RegistryObject<SoundEvent> PREGNANCY_PAIN1 = REGISTRY.register("pregnancy_pain1", () -> SoundEvent.createVariableRangeEvent(MinepreggoHelper.fromNamespaceAndPath(MinepreggoMod.MODID, "pregnancy_pain1")));
	public static final RegistryObject<SoundEvent> PREGNANCY_PAIN2 = REGISTRY.register("pregnancy_pain2", () -> SoundEvent.createVariableRangeEvent(MinepreggoHelper.fromNamespaceAndPath(MinepreggoMod.MODID, "pregnancy_pain2")));
	public static final RegistryObject<SoundEvent> PREGNANCY_PAIN3 = REGISTRY.register("pregnancy_pain3", () -> SoundEvent.createVariableRangeEvent(MinepreggoHelper.fromNamespaceAndPath(MinepreggoMod.MODID, "pregnancy_pain3")));
	public static final RegistryObject<SoundEvent> PREGNANCY_PAIN4 = REGISTRY.register("pregnancy_pain4", () -> SoundEvent.createVariableRangeEvent(MinepreggoHelper.fromNamespaceAndPath(MinepreggoMod.MODID, "pregnancy_pain4")));
		
	public static final RegistryObject<SoundEvent> PLAYER_BIRTH = REGISTRY.register("player_birth", () -> SoundEvent.createVariableRangeEvent(MinepreggoHelper.fromNamespaceAndPath(MinepreggoMod.MODID, "player_birth")));
	public static final RegistryObject<SoundEvent> PLAYER_CONTRACTION = REGISTRY.register("player_contraction", () -> SoundEvent.createVariableRangeEvent(MinepreggoHelper.fromNamespaceAndPath(MinepreggoMod.MODID, "player_contraction")));
	public static final RegistryObject<SoundEvent> PLAYER_MISCARRIAGE = REGISTRY.register("player_miscarriage", () -> SoundEvent.createVariableRangeEvent(MinepreggoHelper.fromNamespaceAndPath(MinepreggoMod.MODID, "player_miscarriage")));

	public static final RegistryObject<SoundEvent> PREGNANT_STOMACH_ACHING = REGISTRY.register("pregnant_stomach_aching", () -> SoundEvent.createVariableRangeEvent(MinepreggoHelper.fromNamespaceAndPath(MinepreggoMod.MODID, "pregnant_stomach_aching")));
	public static final RegistryObject<SoundEvent> PREGNANT_STOMACH_DIGESTING = REGISTRY.register("pregnant_stomach_digesting", () -> SoundEvent.createVariableRangeEvent(MinepreggoHelper.fromNamespaceAndPath(MinepreggoMod.MODID, "pregnant_stomach_digesting")));
	public static final RegistryObject<SoundEvent> PREGNANT_STOMACH_EMPTY = REGISTRY.register("pregnant_stomach_empty", () -> SoundEvent.createVariableRangeEvent(MinepreggoHelper.fromNamespaceAndPath(MinepreggoMod.MODID, "pregnant_stomach_empty")));
	public static final RegistryObject<SoundEvent> PREGNANT_STOMACH_FULL = REGISTRY.register("pregnant_stomach_full", () -> SoundEvent.createVariableRangeEvent(MinepreggoHelper.fromNamespaceAndPath(MinepreggoMod.MODID, "pregnant_stomach_full")));
	public static final RegistryObject<SoundEvent> PREGNANT_STOMACH_SATED = REGISTRY.register("pregnant_stomach_sated", () -> SoundEvent.createVariableRangeEvent(MinepreggoHelper.fromNamespaceAndPath(MinepreggoMod.MODID, "pregnant_stomach_sated")));
	public static final RegistryObject<SoundEvent> PREGNANT_STOMACH_UPSET = REGISTRY.register("pregnant_stomach_upset", () -> SoundEvent.createVariableRangeEvent(MinepreggoHelper.fromNamespaceAndPath(MinepreggoMod.MODID, "pregnant_stomach_upset")));

	public static final RegistryObject<SoundEvent> PREGNANT_WOMB_SWELL = REGISTRY.register("pregnant_womb_swell", () -> SoundEvent.createVariableRangeEvent(MinepreggoHelper.fromNamespaceAndPath(MinepreggoMod.MODID, "pregnant_womb_swell")));

	public static final RegistryObject<SoundEvent> PLAYER_PUSH_1 = REGISTRY.register("player_push_1", () -> SoundEvent.createVariableRangeEvent(MinepreggoHelper.fromNamespaceAndPath(MinepreggoMod.MODID, "player_push_1")));
	public static final RegistryObject<SoundEvent> PLAYER_PUSH_2 = REGISTRY.register("player_push_2", () -> SoundEvent.createVariableRangeEvent(MinepreggoHelper.fromNamespaceAndPath(MinepreggoMod.MODID, "player_push_2")));
	public static final RegistryObject<SoundEvent> PLAYER_PUSH_3 = REGISTRY.register("player_push_3", () -> SoundEvent.createVariableRangeEvent(MinepreggoHelper.fromNamespaceAndPath(MinepreggoMod.MODID, "player_push_3")));
	
	public static final SoundEvent getRandomPregnancyPain(RandomSource random) {	
		return switch (random.nextInt(4)) {
			case 0 -> PREGNANCY_PAIN1.get();
			case 1 -> PREGNANCY_PAIN2.get();
			case 2 -> PREGNANCY_PAIN3.get();
			default -> PREGNANCY_PAIN4.get();
		};
	}
	
	public static final SoundEvent getRandomStomachGrowls(RandomSource random) {
		var rand = random;
		return PREGNANT_STOMACH_FULL.get();
	}
	
	public static final SoundEvent getRandomPlayerPush(RandomSource random) {
		return switch (random.nextInt(3)) {
			case 0 -> PLAYER_PUSH_1.get();
			case 1 -> PLAYER_PUSH_2.get();
			default -> PLAYER_PUSH_3.get();
		};
	}
}