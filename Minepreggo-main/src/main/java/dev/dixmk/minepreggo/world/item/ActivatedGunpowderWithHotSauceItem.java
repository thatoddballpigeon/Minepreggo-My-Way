package dev.dixmk.minepreggo.world.item;

import javax.annotation.Nonnegative;

import dev.dixmk.minepreggo.world.entity.preggo.Species;
import dev.dixmk.minepreggo.world.pregnancy.Craving;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ActivatedGunpowderWithHotSauceItem extends ActivatedGunpowderItem implements ICravingItem {
	public ActivatedGunpowderWithHotSauceItem() {
		super(11);
	}
	
	@Override
	@Nonnegative
	public int getGratification() {
		return 8;
	}
	
	@Override
	public Craving getCravingType() {
		return Craving.SPICY;
	}
	
	@Override
	public Species getSpeciesType() {
		return Species.CREEPER;
	}

	@Override
	public @Nonnegative float getPenalty() {
		return 0.3f;
	}

	@Override
	public @NotNull ItemStack finishUsingItem(@NotNull ItemStack p_40684_, @NotNull Level p_40685_, @NotNull LivingEntity p_40686_) {
		ItemHelper.finishUsingSpicyItem(p_40686_);
		return super.finishUsingItem(p_40684_, p_40685_, p_40686_);
	}
}
