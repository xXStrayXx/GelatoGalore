package com.itayfeder.gelato_galore.init;

import com.itayfeder.gelato_galore.GelatoGalore;
import com.itayfeder.gelato_galore.recipes.IceCreamBuildRecipe;
import com.itayfeder.gelato_galore.recipes.IceCreamSandwichRecipe;
import com.itayfeder.gelato_galore.recipes.IceCreamToppingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeInit {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, GelatoGalore.MODID);

    public static final RegistryObject<RecipeSerializer<IceCreamToppingRecipe>> ICE_CREAM_TOPPING = RECIPE_SERIALIZERS.register("special_icecreamtopping", () -> new SimpleCraftingRecipeSerializer<>(IceCreamToppingRecipe::new));
    public static final RegistryObject<RecipeSerializer<IceCreamSandwichRecipe>> ICE_CREAM_SANDWICH = RECIPE_SERIALIZERS.register("special_icecreamsandwich", () -> new SimpleCraftingRecipeSerializer<>(IceCreamSandwichRecipe::new));
    public static final RegistryObject<RecipeSerializer<IceCreamBuildRecipe>> ICE_CREAM_BUILD = RECIPE_SERIALIZERS.register("special_icecreambuild", () -> new SimpleCraftingRecipeSerializer<>(IceCreamBuildRecipe::new));

}
