package dev.dixmk.minepreggo.world.item;

import dev.dixmk.minepreggo.init.MinepreggoCapabilities;
import dev.dixmk.minepreggo.init.MinepreggoModMobEffects;
import dev.dixmk.minepreggo.init.MinepreggoModSounds;
import dev.dixmk.minepreggo.world.entity.LivingEntityHelper;
import dev.dixmk.minepreggo.world.entity.player.PlayerHelper;
import dev.dixmk.minepreggo.world.entity.preggo.*;
import dev.dixmk.minepreggo.world.pregnancy.PregnancyPain;
import dev.dixmk.minepreggo.world.pregnancy.PregnancyPhase;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.NotNull;

public class ItemHelper {

	private ItemHelper() {}
		
	public static boolean isHelmet(ItemStack item) {
		if (item == null) return false;
		return isHelmet(item.getItem());
	}
	
	public static boolean isHelmet(Item item) {
		return item instanceof ArmorItem armorItem && armorItem.getEquipmentSlot() == EquipmentSlot.HEAD;
	}
	
	public static boolean isBoot(ItemStack item) {
		if (item == null) return false;
		return isBoot(item.getItem());
	}
	
	public static boolean isBoot(Item item) {
		return item instanceof ArmorItem armorItem && armorItem.getEquipmentSlot() == EquipmentSlot.FEET;
	}
	
	public static boolean isLegging(ItemStack item) {
		if (item == null) return false;	
		return isLegging(item.getItem());
	}
	
	public static boolean isLegging(Item item) {
		return item instanceof ArmorItem armorItem && armorItem.getEquipmentSlot() == EquipmentSlot.LEGS;
	}
	
	public static boolean isChest(ItemStack item) {
		if (item == null) return false;	
		return isChest(item.getItem());
	}
	
	public static boolean isChest(Item item) {
		return item instanceof ArmorItem armorItem && armorItem.getEquipmentSlot() == EquipmentSlot.CHEST;
	}
	
	public static boolean isMilk(ItemStack item) {
		if (item == null) return false;	
		return isMilk(item.getItem());
	}
	
	public static boolean isMilk(Item item) {
    	return item == Items.MILK_BUCKET || item instanceof AbstractBreastMilk;
	}
	
	public static Item getRandomHelmet(RandomSource random) {
		return switch (random.nextInt(5)) {
		case 0 -> Items.LEATHER_HELMET;
		case 1 -> Items.IRON_HELMET;
		case 2 -> Items.GOLDEN_HELMET;
		case 3 -> Items.DIAMOND_HELMET;
		default -> Items.CHAINMAIL_HELMET;
		};
	}
	
	public static Item getRandomAxe(RandomSource random) {
		return switch (random.nextInt(5)) {
		case 0 -> Items.WOODEN_AXE;
		case 1 -> Items.STONE_AXE;
		case 2 -> Items.IRON_AXE;
		case 3 -> Items.GOLDEN_AXE;
		default -> Items.DIAMOND_AXE;
		};
	}
	
	public static boolean canPickUp(Player player, ItemStack itemstack) {
	    return PlayerHelper.isInvencible(player) || itemstack.getEnchantmentLevel(Enchantments.BINDING_CURSE) == 0;	
	}

	public static void finishUsingSpicyItem(@NotNull LivingEntity p_40686_) {
		if (p_40686_ instanceof ServerPlayer player) {
			player.getCapability(MinepreggoCapabilities.PLAYER_DATA).ifPresent(cap -> {
				cap.getFemaleData().ifPresent(femaleData -> {
					var pregnancySystem = femaleData.getPregnancyData();
					if (femaleData.isPregnant() && femaleData.isPregnancyDataInitialized()
							&& !player.hasEffect(MinepreggoModMobEffects.FETAL_MOVEMENT.get())
							&& !player.hasEffect(MinepreggoModMobEffects.CONTRACTION.get())) {
						int i = player.getRandom().nextInt(2);
						if (i == 0 && pregnancySystem.getCurrentPregnancyPhase().compareTo(PregnancyPhase.P2) > 0) {
							pregnancySystem.setPregnancyPain(PregnancyPain.FETAL_MOVEMENT);
							pregnancySystem.resetPregnancyPainTimer();
							LivingEntityHelper.playSoundNearTo(player, MinepreggoModSounds.PLAYER_CONTRACTION.get());
							player.addEffect(new MobEffectInstance(MinepreggoModMobEffects.FETAL_MOVEMENT.get(), 600, 0, false, false, true));
							PlayerHelper.removeAndDropItemStackFromEquipmentSlot(player, EquipmentSlot.CHEST);
						} else if (pregnancySystem.getCurrentPregnancyPhase().compareTo(PregnancyPhase.P3) > 0) {
							pregnancySystem.setPregnancyPain(PregnancyPain.CONTRACTION);
							pregnancySystem.resetPregnancyPainTimer();
							LivingEntityHelper.playSoundNearTo(player, MinepreggoModSounds.PLAYER_CONTRACTION.get());
							player.addEffect(new MobEffectInstance(MinepreggoModMobEffects.CONTRACTION.get(), 600, 0, false, false, true));
							PlayerHelper.removeAndDropItemStackFromEquipmentSlot(player, EquipmentSlot.CHEST);
						}
						pregnancySystem.syncState(player);
					}
				});
			});
		} else if (p_40686_ instanceof ITamablePregnantPreggoMob handler) {
			var pregnancySystem = handler.getPregnancyData();
			if (!p_40686_.hasEffect(MinepreggoModMobEffects.FETAL_MOVEMENT.get())
				&& !p_40686_.hasEffect(MinepreggoModMobEffects.CONTRACTION.get())) {
				int i = p_40686_.getRandom().nextInt(2);
				if (i == 0 && pregnancySystem.getCurrentPregnancyPhase().compareTo(PregnancyPhase.P2) > 0) {
					pregnancySystem.setPregnancyPain(PregnancyPain.FETAL_MOVEMENT);
					pregnancySystem.resetPregnancyPainTimer();
					PreggoMobHelper.removeAndDropItemStackFromEquipmentSlot((PreggoMob & ITamablePreggoMob<?>) p_40686_, InventorySlot.CHEST);
					LivingEntityHelper.playStomachGrowlSound(p_40686_, p_40686_.getId(), 0);
				} else if (pregnancySystem.getCurrentPregnancyPhase().compareTo(PregnancyPhase.P3) > 0) {
					pregnancySystem.setPregnancyPain(PregnancyPain.CONTRACTION);
					pregnancySystem.resetPregnancyPainTimer();
					PreggoMobHelper.removeAndDropItemStackFromEquipmentSlot((PreggoMob & ITamablePreggoMob<?>) p_40686_, InventorySlot.CHEST);
					LivingEntityHelper.playStomachGrowlSound(p_40686_, p_40686_.getId(), 0);
				}
			}
		}
	}
}
