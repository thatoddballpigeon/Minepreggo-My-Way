package dev.dixmk.minepreggo.world.item;

import javax.annotation.Nonnegative;

import dev.dixmk.minepreggo.world.entity.preggo.Species;
import dev.dixmk.minepreggo.world.pregnancy.Craving;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ChorusFruitItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ChorusFruitWithHotSauceItem extends ChorusFruitItem implements ICravingItem {
	public ChorusFruitWithHotSauceItem() {
		super((new Item.Properties()).food((new FoodProperties.Builder()).nutrition(7).saturationMod(0.3F).alwaysEat().build()));
	}
	
	@Override
	@Nonnegative
	public int getGratification() {
		return 10;
	}
	
	@Override
	public Craving getCravingType() {
		return Craving.SPICY;
	}
	
	@Override
	public Species getSpeciesType() {
		return Species.DRAGON;
	}

	@Override
	public @Nonnegative float getPenalty() {
		return 0.1f;
	}

	@Override
	public @NotNull ItemStack finishUsingItem(@NotNull ItemStack p_40684_, @NotNull Level p_40685_, @NotNull LivingEntity p_40686_) {
		ItemHelper.finishUsingSpicyItem(p_40686_);
		return super.finishUsingItem(p_40684_, p_40685_, p_40686_);
	}
}
