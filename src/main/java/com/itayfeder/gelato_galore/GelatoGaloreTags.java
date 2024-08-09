package com.itayfeder.gelato_galore;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GelatoGaloreTags {
    public static class BlockTags {

        private static TagKey<Block> create(String name) {
            return TagKey.create(ForgeRegistries.Keys.BLOCKS, new ResourceLocation(GelatoGalore.MODID, name));
        }
    }

    public static class ItemTags {
        public static final List<TagKey<Item>> INGREDIENT_TAGS = new ArrayList<>();
        public static final Map<TagKey<Item>, String[]> MODDED_INGREDIENT_TAGS = new HashMap<>();

        public static final TagKey<Item> CHOCOLATE_INGREDIENT = createIngredientTag("chocolate_flavor");
        public static final TagKey<Item> SWEET_BERRY_INGREDIENT = createIngredientTag("sweet_berry_flavor");
        public static final TagKey<Item> CARAMEL_INGREDIENT = createIngredientTag("caramel_flavor");
        public static final TagKey<Item> HONEY_INGREDIENT = createIngredientTag("honey_flavor");
        public static final TagKey<Item> VANILLA_INGREDIENT = createIngredientTag("vanilla_flavor");
        public static final TagKey<Item> MELON_INGREDIENT = createIngredientTag("melon_flavor");
        public static final TagKey<Item> CHORUS_INGREDIENT = createIngredientTag("chorus_flavor");

        public static final TagKey<Item> COMPAT_COFFEE_INGREDIENT = createModdedIngredientTag("compat/coffee_flavor", "farmersrespite");
        public static final TagKey<Item> COMPAT_GREEN_TEA_INGREDIENT = createModdedIngredientTag("compat/green_tea_flavor", "farmersrespite");
        public static final TagKey<Item> COMPAT_BANANA_INGREDIENT = createModdedIngredientTag("compat/banana_flavor", "neapolitan");
        public static final TagKey<Item> COMPAT_MINT_INGREDIENT = createModdedIngredientTag("compat/mint_flavor", "neapolitan");
        public static final TagKey<Item> COMPAT_ADZUKI_INGREDIENT = createModdedIngredientTag("compat/adzuki_flavor", "neapolitan");
        public static final TagKey<Item> COMPAT_STRAWBERRY_INGREDIENT = createModdedIngredientTag("compat/strawberry_flavor", "neapolitan");

        public static final TagKey<Item> FORGE_MILK = createForgeTag("milk");

        private static TagKey<Item> createIngredientTag(String name) {
            TagKey<Item> itemtag = TagKey.create(ForgeRegistries.Keys.ITEMS, new ResourceLocation(GelatoGalore.MODID, name));
            INGREDIENT_TAGS.add(itemtag);
            return itemtag;
        }

        private static TagKey<Item> createModdedIngredientTag(String name, String... mods) {
            TagKey<Item> itemtag = TagKey.create(ForgeRegistries.Keys.ITEMS, new ResourceLocation(GelatoGalore.MODID, name));
            MODDED_INGREDIENT_TAGS.put(itemtag, mods);
            return itemtag;
        }

        private static TagKey<Item> createForgeTag(String name) {
            TagKey<Item> itemtag = TagKey.create(ForgeRegistries.Keys.ITEMS, new ResourceLocation("forge", name));
            return itemtag;
        }
    }
}
