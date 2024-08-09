package com.itayfeder.gelato_galore.reload;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FlavorData {
    public static int CURRENT_MAX_ID = 0;

    public static final Codec<FlavorData> CODEC = RecordCodecBuilder.create(instance -> // Given an instance
            instance.group( // Define the fields within the instance
                    ResourceLocation.CODEC.fieldOf("id").forGetter(FlavorData::getId),
                    Codec.STRING.fieldOf("name").forGetter(FlavorData::getName),
                    Codec.INT.optionalFieldOf("color", 0).forGetter(FlavorData::getColor),
                    FlavorEffect.CODEC.optionalFieldOf("effect", null).forGetter(FlavorData::getEffect),
                    Codec.INT.optionalFieldOf("patternId", 0).forGetter(FlavorData::getPatternId),
                    Codec.INT.optionalFieldOf("patternColor", 0).forGetter(FlavorData::getPatternColor),
                    Codec.list(Codec.STRING).optionalFieldOf("requiredMods", new ArrayList<>()).forGetter(FlavorData::getRequiredMods)
            ).apply(instance, FlavorData::new) // Define how to create the object
    );

    public ResourceLocation id;
    public String name;
    public int color;
    public FlavorEffect effect;
    public int patternId;
    public int patternColor;
    public List<String> requiredMods;

    public FlavorData(ResourceLocation id, String name, int color, FlavorEffect instance, int patternId, int patternColor, List<String> requiredMods) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.effect = instance;
        this.patternId = patternId;
        this.patternColor = patternColor;
        this.requiredMods = requiredMods;
    }

    public FlavorData(ResourceLocation id, String name, int color, ResourceLocation location, int duration, int amplifier, int patternId, int patternColor, List<String> requiredMods) {
        this.id = id;
        this.name = name;
        this.color = color;
        MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(location);
        if (effect != null)
            this.effect = new FlavorEffect(effect, duration, amplifier);
        this.patternId = patternId;
        this.patternColor = patternColor;
        this.requiredMods = requiredMods;
    }

    public ResourceLocation getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public FlavorEffect getEffect() {
        return effect;
    }

    public int getPatternId() {
        return patternId;
    }

    public int getPatternColor() {
        return patternColor;
    }


    public List<String> getRequiredMods() {
        return requiredMods;
    }

    public Color getAsColor() {
        String hexColor = String.format("#%06X", (0xFFFFFF & this.color));
        Color color = Color.decode(hexColor);
        return color;
    }

    public static FlavorData deserialize(JsonObject json, ResourceLocation origin) {
        if (!json.isJsonObject())
            throw new JsonParseException("FlavorData must be a JSON Object");
        JsonObject jsonObject = json.getAsJsonObject();

        List<String> requiredMods = new ArrayList<>();
        JsonArray jsonModList = GsonHelper.getAsJsonArray(jsonObject, "requiredMods", new JsonArray());
        for (JsonElement element : jsonModList) {
            requiredMods.add(element.getAsString());
        }
        boolean supportedModsLoaded = false;
        for (String mod : requiredMods) {
            if(!ModList.get().isLoaded(mod)) {
                supportedModsLoaded = true;
            }
        }
        if (supportedModsLoaded) {
            return null;
        }

        String name = GsonHelper.getAsString(jsonObject, "name", null);
        if (name == null)
            throw new JsonParseException("name is not valid");

        int color = GsonHelper.getAsInt(jsonObject, "color", 0);
        if (color < 0)
            throw new JsonParseException("color is not valid");

        JsonElement effectElement = json.get("effectInstance");
        FlavorEffect instance = null;
        if (effectElement != null) {
            String effectStr = GsonHelper.getAsString(effectElement.getAsJsonObject(), "effect", null);
            ResourceLocation location = ResourceLocation.tryParse(effectStr != null ? effectStr : "");
            if (location != null) {
                int duration = GsonHelper.getAsInt(effectElement.getAsJsonObject(), "duration", 900);
                int amplifier = GsonHelper.getAsInt(effectElement.getAsJsonObject(), "amplifier", 0);

                MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(location);
                instance = new FlavorEffect(effect != null ? effect : MobEffects.MOVEMENT_SPEED, duration, amplifier);
            }

        }

        int patternId = GsonHelper.getAsInt(jsonObject, "patternId", 0);
        int patternColor = GsonHelper.getAsInt(jsonObject, "patternColor", color);

        return new FlavorData(origin, name, color, instance, patternId, patternColor, requiredMods);

    }

    public static class FlavorEffect {
        private final MobEffect effect;
        public int duration;
        private int amplifier;

        public static final Codec<FlavorEffect> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        ForgeRegistries.MOB_EFFECTS.getCodec().fieldOf("effect").forGetter(FlavorEffect::getEffect),
                        Codec.INT.optionalFieldOf("duration", 900).forGetter(FlavorEffect::getDuration),
                        Codec.INT.optionalFieldOf("amplifier", 0).forGetter(FlavorEffect::getAmplifier)
                ).apply(instance, FlavorEffect::new)
        );

        public FlavorEffect(MobEffect p_19513_) {
            this(p_19513_, 0, 0);
        }

        public FlavorEffect(MobEffect p_19515_, int p_19516_) {
            this(p_19515_, p_19516_, 0);
        }

        public FlavorEffect(MobEffect p_216887_, int p_216888_, int p_216889_) {
            this.effect = p_216887_;
            this.duration = p_216888_;
            this.amplifier = p_216889_;
        }

        public MobEffectInstance construct() {
            return new MobEffectInstance(this.effect, this.duration, this.amplifier);
        }

        public MobEffectInstance constructModified(int durationMod, int amplifierMod) {
            return new MobEffectInstance(this.effect, this.duration + durationMod, this.amplifier + amplifierMod);
        }

        public MobEffect getEffect() {
            return effect;
        }

        public int getDuration() {
            return duration;
        }

        public int getAmplifier() {
            return amplifier;
        }
    }
}
