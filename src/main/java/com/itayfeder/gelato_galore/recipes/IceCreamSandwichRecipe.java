package com.itayfeder.gelato_galore.recipes;

import com.itayfeder.gelato_galore.init.ItemInit;
import com.itayfeder.gelato_galore.init.RecipeInit;
import com.itayfeder.gelato_galore.items.IceCreamSandwichItem;
import com.itayfeder.gelato_galore.items.ScooperItem;
import com.itayfeder.gelato_galore.reload.FlavorData;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class IceCreamSandwichRecipe extends CustomRecipe {
    public IceCreamSandwichRecipe(ResourceLocation p_252125_, CraftingBookCategory p_249010_) {
        super(p_252125_, p_249010_);
    }

    @Override
    public boolean matches(CraftingContainer craftingContainer, Level level) {
        int start_index = 0;
        for(int k = 0; k < craftingContainer.getContainerSize(); ++k) {
            ItemStack itemstack = craftingContainer.getItem(k);
            if (k < 3 && itemstack.getItem() == Items.COOKIE) {
                start_index = k;
                return ((craftingContainer.getItem(k + 3).getItem() == ItemInit.SCOOPER.get()) &&
                        (craftingContainer.getItem(k + 6).getItem() == Items.COOKIE));
            }
        }
        return false;
    }

    @Override
    public ItemStack assemble(CraftingContainer craftingContainer, RegistryAccess registryAccess) {
        int start_index = 0;
        for(int k = 0; k < craftingContainer.getContainerSize(); ++k) {
            ItemStack itemstack = craftingContainer.getItem(k);
            if (k < 3 && itemstack.getItem() == Items.COOKIE) {
                start_index = k;
                k = 10;
            }
        }

        ItemStack scooper_itemstack = craftingContainer.getItem(start_index + 3);
        ItemStack sandwich_stack = new ItemStack(ItemInit.ICE_CREAM_SANDWICH.get());

        FlavorData flavor = ScooperItem.getFilledFlavor(scooper_itemstack);
        IceCreamSandwichItem.setFilled(sandwich_stack, flavor.id);

        return sandwich_stack;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return i * i1 >= 5;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeInit.ICE_CREAM_SANDWICH.get();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer p_44004_) {
        return super.getRemainingItems(p_44004_);
    }
}
