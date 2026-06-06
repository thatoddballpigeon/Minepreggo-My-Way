package dev.dixmk.minepreggo.world.item;

import javax.annotation.Nonnegative;

import dev.dixmk.minepreggo.world.entity.preggo.Species;
import dev.dixmk.minepreggo.world.pregnancy.Craving;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BrainWithHotSauceItem extends BrainItem implements ICravingItem {
	public BrainWithHotSauceItem() {
		super(15, Rarity.COMMON);
	}
	
	@Override
	@Nonnegative
	public int getGratification() {
		return 9;
	}
	
	@Override
	public Craving getCravingType() {
		return Craving.SPICY;
	}
	
	@Override
	public Species getSpeciesType() {
		return Species.ZOMBIE;
	}

	@Override
	public @Nonnegative float getPenalty() {
		return 0.2f;
	}

	@Override
	public @NotNull ItemStack finishUsingItem(@NotNull ItemStack p_40684_, @NotNull Level p_40685_, @NotNull LivingEntity p_40686_) {
		ItemHelper.finishUsingSpicyItem(p_40686_);
		return super.finishUsingItem(p_40684_, p_40685_, p_40686_);
	}
}
