package dev.dixmk.minepreggo.world.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class ChiliPepperItem extends ItemNameBlockItem {
	public ChiliPepperItem(Block p_41579_, Item.Properties p_41580_) {
		super(p_41579_, new Item.Properties().stacksTo(64).rarity(Rarity.COMMON).food((new FoodProperties.Builder()).nutrition(1).saturationMod(0.1f).build()));
	}

	@Override
	public int getUseDuration(ItemStack itemstack) {
		return 70;
	}

	@Override
	public @NotNull ItemStack finishUsingItem(@NotNull ItemStack p_40684_, @NotNull Level p_40685_, @NotNull LivingEntity p_40686_) {
		ItemHelper.finishUsingSpicyItem(p_40686_);
		return super.finishUsingItem(p_40684_, p_40685_, p_40686_);
	}
}
