package com.itayfeder.gelato_galore.items.api;

import com.itayfeder.gelato_galore.toppings.Topping;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ToppingItem extends Item {
    public final Topping topping;
    public final boolean durable;

    public ToppingItem(Properties p_41383_, Topping topping, boolean durable) {
        super(p_41383_);
        this.topping = topping;
        this.durable = durable;
    }

    public Topping getTopping(ItemStack stack) {
        return topping.copy();
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return this.durable;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        if (this.durable) {
            ItemStack copyStack = itemStack.copy();
            RandomSource source = RandomSource.create();
            if (copyStack.hurt(1, source, null)) {
                Item item = copyStack.getItem();
                copyStack.shrink(1);
                copyStack.setDamageValue(0);
            }
            return copyStack;
        }
        return super.getCraftingRemainingItem(itemStack);
    }
}
