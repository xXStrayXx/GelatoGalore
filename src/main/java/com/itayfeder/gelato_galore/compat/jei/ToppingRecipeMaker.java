package com.itayfeder.gelato_galore.compat.jei;

import com.itayfeder.gelato_galore.GelatoGalore;
import com.itayfeder.gelato_galore.init.ItemInit;
import com.itayfeder.gelato_galore.items.IceCreamItem;
import com.itayfeder.gelato_galore.toppings.Topping;
import com.itayfeder.gelato_galore.toppings.Toppings;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;

public class ToppingRecipeMaker {
    public static List<CraftingRecipe> createRecipes() {
        List<CraftingRecipe> recipe = new ArrayList<>();
        ItemLike[] vals = new ItemLike[]
                {ItemInit.ICE_CREAM_ONE.get(), ItemInit.ICE_CREAM_TWO.get(), ItemInit.ICE_CREAM_THREE.get()};
        for (Topping topping : Topping.TOPPINGS.values()) {
            if (!topping.location.equals(Toppings.EMPTY.location))
            for (int i = 0; i < vals.length; i++) {
                String group = "jei.topping." + topping.location.getPath() + "_" + (i + 1);
                ResourceLocation id = new ResourceLocation(GelatoGalore.MODID, group);
                ItemStack stack = vals[i].asItem().getDefaultInstance();
                ((IceCreamItem) vals[i].asItem()).defaultBuild(stack);
                Ingredient ice_cream = Ingredient.of(stack);
                Ingredient top_item = Ingredient.of(topping.getItemstack());

                NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, new Ingredient[]{ice_cream, top_item});

                ItemStack output = vals[i].asItem().getDefaultInstance();
                ((IceCreamItem) vals[i].asItem()).defaultBuild(output);
                IceCreamItem.setTopping(output, topping);

                recipe.add(new ShapelessRecipe(id, group, CraftingBookCategory.MISC, output, inputs));
            }
        }
        return recipe;
    }
}
