package com.itayfeder.gelato_galore.data.tags;

import com.itayfeder.gelato_galore.GelatoGalore;
import com.itayfeder.gelato_galore.GelatoGaloreTags;
import com.itayfeder.gelato_galore.init.ItemInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class GelatoItemTagsGen extends ItemTagsProvider {
    public GelatoItemTagsGen(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, GelatoGalore.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(GelatoGaloreTags.ItemTags.CHOCOLATE_INGREDIENT).add(Items.COCOA_BEANS);
        this.tag(GelatoGaloreTags.ItemTags.SWEET_BERRY_INGREDIENT).add(Items.SWEET_BERRIES);
        this.tag(GelatoGaloreTags.ItemTags.HONEY_INGREDIENT).add(Items.HONEY_BOTTLE);
        this.tag(GelatoGaloreTags.ItemTags.CARAMEL_INGREDIENT).add(ItemInit.CARAMEL.get());
        this.tag(GelatoGaloreTags.ItemTags.VANILLA_INGREDIENT).add(ItemInit.DRIED_VANILLA_PODS.get())
                .addOptional(new ResourceLocation("neapolitan", "dried_vanilla_pods"));
        this.tag(GelatoGaloreTags.ItemTags.MELON_INGREDIENT).add(Items.MELON_SLICE);
        this.tag(GelatoGaloreTags.ItemTags.CHORUS_INGREDIENT).add(Items.CHORUS_FRUIT);

        this.tag(GelatoGaloreTags.ItemTags.FORGE_MILK).add(Items.MILK_BUCKET);


        this.tag(GelatoGaloreTags.ItemTags.COMPAT_COFFEE_INGREDIENT)
                .addOptional(new ResourceLocation("farmersrespite", "coffee_beans"));
        this.tag(GelatoGaloreTags.ItemTags.COMPAT_GREEN_TEA_INGREDIENT)
                .addOptional(new ResourceLocation("farmersrespite", "green_tea_leaves"));
        this.tag(GelatoGaloreTags.ItemTags.COMPAT_BANANA_INGREDIENT)
                .addOptional(new ResourceLocation("neapolitan", "banana"));
        this.tag(GelatoGaloreTags.ItemTags.COMPAT_MINT_INGREDIENT)
                .addOptional(new ResourceLocation("neapolitan", "mint_leaves"));
        this.tag(GelatoGaloreTags.ItemTags.COMPAT_ADZUKI_INGREDIENT)
                .addOptional(new ResourceLocation("neapolitan", "roasted_adzuki_beans"));
        this.tag(GelatoGaloreTags.ItemTags.COMPAT_STRAWBERRY_INGREDIENT)
                .addOptional(new ResourceLocation("neapolitan", "strawberries"));
    }
}
