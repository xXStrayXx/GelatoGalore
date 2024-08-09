package com.itayfeder.gelato_galore.data.tags;

import com.itayfeder.gelato_galore.GelatoGalore;
import com.itayfeder.gelato_galore.init.BlockInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class GelatoBlockTagsGen extends BlockTagsProvider {

    public GelatoBlockTagsGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, GelatoGalore.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BlockInit.ICE_CREAM_CAULDRON.get());
        this.tag(BlockTags.CAULDRONS).add(BlockInit.ICE_CREAM_CAULDRON.get());

        this.tag(BlockTags.MINEABLE_WITH_AXE).add(BlockInit.VANILLA_VINE.get());
        this.tag(BlockTags.FALL_DAMAGE_RESETTING).add(BlockInit.VANILLA_VINE.get());
        this.tag(BlockTags.FALL_DAMAGE_RESETTING).add(BlockInit.VANILLA_VINE.get());

    }
}
