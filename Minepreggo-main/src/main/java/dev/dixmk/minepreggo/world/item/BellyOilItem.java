package dev.dixmk.minepreggo.world.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BellyOilItem extends Item {
    public BellyOilItem() {
        super(new Item.Properties().stacksTo(16).rarity(Rarity.COMMON));
    }

    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack p_40684_, @NotNull Level p_40685_, @NotNull LivingEntity p_40686_) {
        ItemStack itemstack = super.finishUsingItem(p_40684_, p_40685_, p_40686_);
        return p_40686_ instanceof Player && ((Player)p_40686_).getAbilities().instabuild ? itemstack : new ItemStack(Items.GLASS_BOTTLE);
    }
}
