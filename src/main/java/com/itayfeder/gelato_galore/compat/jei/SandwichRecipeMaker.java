package com.itayfeder.gelato_galore.compat.jei;

import com.google.gson.JsonObject;
import com.itayfeder.gelato_galore.GelatoGalore;
import com.itayfeder.gelato_galore.init.ItemInit;
import com.itayfeder.gelato_galore.items.ScooperItem;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;

import java.util.ArrayList;
import java.util.List;

public class SandwichRecipeMaker {
    public static List<CraftingRecipe> createRecipes() {
        List<CraftingRecipe> recipe = new ArrayList<>();
        String group = "jei.item.ice_cream_sandwich";
        ResourceLocation id = new ResourceLocation(GelatoGalore.MODID, group);

        ItemStack scooper_item = ItemInit.SCOOPER.get().getDefaultInstance();
        ScooperItem.setFilled(scooper_item, new ResourceLocation("gelato_galore:chocolate"));

        Ingredient top_item = Ingredient.of(scooper_item);
        NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.COOKIE.getDefaultInstance()),
                top_item,
                Ingredient.of(Items.COOKIE.getDefaultInstance())
        );

        ItemStack stack = ItemInit.ICE_CREAM_SANDWICH.get().getDefaultInstance();

        recipe.add(new ShapedRecipe(id, group, CraftingBookCategory.MISC, 1, 3, inputs, stack));
        return recipe;
    }
}
