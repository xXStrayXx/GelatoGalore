package com.itayfeder.gelato_galore.items;

import com.itayfeder.gelato_galore.GelatoGaloreConfig;
import com.itayfeder.gelato_galore.reload.FlavorData;
import com.itayfeder.gelato_galore.reload.FlavorDataReloadListener;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class IceCreamSandwichItem extends Item {
    public IceCreamSandwichItem(Properties p_41383_) {
        super(p_41383_.food((new FoodProperties.Builder()).nutrition(5).saturationMod(0.5F).build()));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack p_41409_, Level p_41410_, LivingEntity p_41411_) {
        if (!p_41410_.isClientSide) {
            if (GelatoGaloreConfig.ServerConfig.ICECREAM_EFFECT.get()) {
                FlavorData flavor = getFilledFlavor(p_41409_);
                MobEffectInstance instance = getFilledFlavor(p_41409_).effect.constructModified(-flavor.effect.duration / 2, 0);
                if (instance != null)
                    p_41411_.addEffect(instance);
            }
        }
        if (p_41409_.getDamageValue() == p_41409_.getMaxDamage() - 1) {
            return super.finishUsingItem(p_41409_, p_41410_, p_41411_);
        } else {
            ItemStack copyStack = p_41409_.copy();
            if (!(p_41411_ instanceof Player) || !((Player)p_41411_).getAbilities().instabuild) {
                p_41409_.setDamageValue(p_41409_.getDamageValue() + 1);
            }

            ItemStack finished_item = super.finishUsingItem(copyStack, p_41410_, p_41411_);
            return p_41409_;
        }

    }


    @Override
    public int getBarColor(ItemStack p_150901_) {
        return getFilledFlavor(p_150901_).color;
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack def = super.getDefaultInstance();
        setFilled(def, ResourceLocation.tryParse("gelato_galore:chocolate"));
        return def;
    }

    public static void setFilled(ItemStack p_220011_0_, ResourceLocation p_220011_1_) {
        CompoundTag compoundnbt = p_220011_0_.getOrCreateTag();
        compoundnbt.putString("Filling", p_220011_1_.toString());
    }

    public static FlavorData getFilledFlavor(ItemStack p_220012_0_) {
        CompoundTag compoundnbt = p_220012_0_.getOrCreateTag();
        ResourceLocation filled = ResourceLocation.tryParse(compoundnbt.getString("Filling"));
        return FlavorDataReloadListener.getSidedMap().get(filled);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        if (getFilledFlavor(p_41421_) == null) p_41423_.add(Component.translatable("flavor." + "unknown", new Object[0]).withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        else p_41423_.add(Component.translatable("flavor." + getFilledFlavor(p_41421_).name, new Object[0]).withStyle(Style.EMPTY.withColor(getFilledFlavor(p_41421_).color)));

        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
    }
}
