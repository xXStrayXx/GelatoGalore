package com.itayfeder.gelato_galore.data.tags;

import com.itayfeder.gelato_galore.GelatoGalore;
import com.itayfeder.gelato_galore.init.villager.PoiTypeInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.PoiTypeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class GelatoPoiTypeTagsGen extends PoiTypeTagsProvider {
    public GelatoPoiTypeTagsGen(PackOutput p_256012_, CompletableFuture<HolderLookup.Provider> p_256617_) {
        super(p_256012_, p_256617_);
    }

    @Override
    protected void addTags(HolderLookup.Provider p_256206_) {
        this.tag(PoiTypeTags.ACQUIRABLE_JOB_SITE).add(new ResourceKey[]{PoiTypeInit.CONFECTIONER_POI.getKey()});

    }
}
