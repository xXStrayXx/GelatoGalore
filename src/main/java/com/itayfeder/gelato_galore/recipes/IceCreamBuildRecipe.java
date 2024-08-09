package com.itayfeder.gelato_galore.recipes;

import com.google.common.collect.Lists;
import com.itayfeder.gelato_galore.init.ItemInit;
import com.itayfeder.gelato_galore.init.RecipeInit;
import com.itayfeder.gelato_galore.items.IceCreamItem;
import com.itayfeder.gelato_galore.items.ScooperItem;
import com.itayfeder.gelato_galore.toppings.Toppings;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.List;

public class IceCreamBuildRecipe extends CustomRecipe {
    public IceCreamBuildRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer p_44002_, Level p_44003_) {
        ItemStack itemstack = ItemStack.EMPTY;
        List<ItemStack> list = Lists.newArrayList();

        for(int i = 0; i < p_44002_.getContainerSize(); ++i) {
            ItemStack itemstack1 = p_44002_.getItem(i);
            if (!itemstack1.isEmpty()) {
                if (itemstack1.is(ItemInit.ICE_CREAM_CONE.get())) {
                    if (!itemstack.isEmpty()) {
                        return false;
                    }

                    itemstack = itemstack1;
                } else {
                    if (!(itemstack1.getItem() instanceof ScooperItem)) {
                        return false;
                    }
                    if (ScooperItem.getFilledFlavor(itemstack1) != null) {
                        list.add(itemstack1);
                    }
                    if (list.size() > 3) {
                        return false;
                    }
                }
            }
        }

        return !itemstack.isEmpty() && !list.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingContainer p_44001_, RegistryAccess registryAccess) {
        List<ItemStack> list = Lists.newArrayList();
        ItemStack itemstack = ItemStack.EMPTY;

        for(int i = 0; i < p_44001_.getContainerSize(); ++i) {
            ItemStack itemstack1 = p_44001_.getItem(i);
            if (!itemstack1.isEmpty()) {
                Item item = itemstack1.getItem();
                if (itemstack1.is(ItemInit.ICE_CREAM_CONE.get())) {
                    if (!itemstack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }

                    itemstack = itemstack1.copy();
                } else {
                    if (!(itemstack1.getItem() instanceof ScooperItem)) {
                        return ItemStack.EMPTY;
                    }
                    if (ScooperItem.getFilledFlavor(itemstack1) != null) {
                        list.add(itemstack1);
                    }
                    if (list.size() > 3) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        if (!itemstack.isEmpty() && !list.isEmpty()) {
            switch (list.size()) {
                case 1:
                default:
                    ItemStack stack = new ItemStack(ItemInit.ICE_CREAM_ONE.get());
                    ((IceCreamItem) ItemInit.ICE_CREAM_ONE.get()).setFlavor(stack, ScooperItem.getFilledFlavor(list.get(0)), 0);
                    IceCreamItem.setTopping(stack, Toppings.EMPTY);
                    return stack;
                case 2:
                    ItemStack stack2 = new ItemStack(ItemInit.ICE_CREAM_TWO.get());
                    ((IceCreamItem) ItemInit.ICE_CREAM_TWO.get()).setFlavor(stack2, ScooperItem.getFilledFlavor(list.get(0)), 0);
                    ((IceCreamItem) ItemInit.ICE_CREAM_TWO.get()).setFlavor(stack2, ScooperItem.getFilledFlavor(list.get(1)), 1);
                    IceCreamItem.setTopping(stack2, Toppings.EMPTY);
                    return stack2;
                case 3:
                    ItemStack stack3 = new ItemStack(ItemInit.ICE_CREAM_THREE.get());
                    ((IceCreamItem) ItemInit.ICE_CREAM_THREE.get()).setFlavor(stack3, ScooperItem.getFilledFlavor(list.get(0)), 0);
                    ((IceCreamItem) ItemInit.ICE_CREAM_THREE.get()).setFlavor(stack3, ScooperItem.getFilledFlavor(list.get(1)), 1);
                    ((IceCreamItem) ItemInit.ICE_CREAM_THREE.get()).setFlavor(stack3, ScooperItem.getFilledFlavor(list.get(2)), 2);
                    IceCreamItem.setTopping(stack3, Toppings.EMPTY);
                    return stack3;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return p_43999_ * p_44000_ >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeInit.ICE_CREAM_BUILD.get();
    }
}
