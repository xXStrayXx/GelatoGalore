package com.itayfeder.gelato_galore.compat.jei;

import com.itayfeder.gelato_galore.GelatoGalore;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

@JeiPlugin
public class GelatoGalorePlugin implements IModPlugin {
    private static final ResourceLocation ID = new ResourceLocation(GelatoGalore.MODID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registry) {
        registry.addRecipes(RecipeTypes.CRAFTING, ToppingRecipeMaker.createRecipes());
        registry.addRecipes(RecipeTypes.CRAFTING, SandwichRecipeMaker.createRecipes());
    }

}
