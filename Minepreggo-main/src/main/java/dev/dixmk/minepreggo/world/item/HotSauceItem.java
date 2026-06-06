package dev.dixmk.minepreggo.world.item;


import dev.dixmk.minepreggo.init.MinepreggoCapabilities;
import dev.dixmk.minepreggo.init.MinepreggoModMobEffects;
import dev.dixmk.minepreggo.init.MinepreggoModSounds;
import dev.dixmk.minepreggo.world.entity.LivingEntityHelper;
import dev.dixmk.minepreggo.world.entity.player.PlayerHelper;
import dev.dixmk.minepreggo.world.pregnancy.PregnancyPain;
import dev.dixmk.minepreggo.world.pregnancy.PregnancyPhase;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class HotSauceItem extends Item {
	public HotSauceItem(Item.Properties food) {
		super(food);
	}

	@Override
	public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemstack) {
		return UseAnim.DRINK;
	}

	public @NotNull ItemStack finishUsingItem(@NotNull ItemStack p_40684_, @NotNull Level p_40685_, @NotNull LivingEntity p_40686_) {
		ItemHelper.finishUsingSpicyItem(p_40686_);
		ItemStack itemstack = super.finishUsingItem(p_40684_, p_40685_, p_40686_);
		return p_40686_ instanceof Player && ((Player)p_40686_).getAbilities().instabuild ? itemstack : new ItemStack(Items.GLASS_BOTTLE);
	}
}
