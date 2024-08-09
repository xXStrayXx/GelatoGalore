package com.itayfeder.gelato_galore.items;

//import com.itayfeder.gelato_galore.entities.projectiles.IceCreamBall;
import com.itayfeder.gelato_galore.entities.projectiles.IceCreamBall;
import com.itayfeder.gelato_galore.reload.FlavorData;
import com.itayfeder.gelato_galore.reload.FlavorDataReloadListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ScooperItem extends Item {
    public ScooperItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack def = super.getDefaultInstance();
        setFilled(def, null);
        return def;
    }

    public static void setFilled(ItemStack p_220011_0_, ResourceLocation p_220011_1_) {
        CompoundTag compoundnbt = p_220011_0_.getOrCreateTag();
        if (p_220011_1_ == null) {
            compoundnbt.putInt("Filling", -1);
            compoundnbt.putBoolean("IsFilled", false);
        }
        else {
            compoundnbt.putString("Filling", p_220011_1_.toString());
            compoundnbt.putBoolean("IsFilled", true);
        }

    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        ItemStack stack = new ItemStack(this, 1);
        setFilled(stack, null);
        return stack;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    public static boolean isfilled(ItemStack p_220012_0_) {
        CompoundTag compoundnbt = p_220012_0_.getOrCreateTag();
        return compoundnbt.getBoolean("IsFilled");
    }

    public static FlavorData getFilledFlavor(ItemStack p_220012_0_) {
        CompoundTag compoundnbt = p_220012_0_.getOrCreateTag();
        boolean IsFilled = compoundnbt.getBoolean("IsFilled");
        ResourceLocation filled = ResourceLocation.tryParse(compoundnbt.getString("Filling"));
        if (!IsFilled) {
            return null;
        }
        return FlavorDataReloadListener.INSTANCE.FLAVOR_MAP.get(filled);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        FlavorData flavor = getFilledFlavor(p_41421_);
        if (flavor != null)
            p_41423_.add(Component.translatable("flavor." + flavor.name, new Object[0]).withStyle(Style.EMPTY.withColor(flavor.color)));

        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
    }

    public int getUseDuration(ItemStack p_40680_) {
        return 72000;
    }

    public UseAnim getUseAnimation(ItemStack p_40678_) {
        return UseAnim.SPEAR;
    }

    public InteractionResultHolder<ItemStack> use(Level p_40672_, Player p_40673_, InteractionHand p_40674_) {
        ItemStack itemstack = p_40673_.getItemInHand(p_40674_);
        boolean flag = getFilledFlavor(itemstack) != null;

        InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, p_40672_, p_40673_, p_40674_, flag);
        if (ret != null) return ret;

        if (!p_40673_.getAbilities().instabuild && !flag) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            p_40673_.startUsingItem(p_40674_);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    @Override
    public void releaseUsing(ItemStack p_41412_, Level p_41413_, LivingEntity p_41414_, int p_41415_) {
        FlavorData flavor = getFilledFlavor(p_41412_);
        if (flavor != null) {
            p_41413_.playSound((Player)null, p_41414_.getX(), p_41414_.getY(), p_41414_.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (p_41414_.getRandom().nextFloat() * 0.4F + 0.8F));
            if (!p_41413_.isClientSide) {
                IceCreamBall snowball = new IceCreamBall(p_41414_, p_41413_);
                snowball.setFlavor(flavor.id);
                snowball.shootFromRotation(p_41414_, p_41414_.getXRot(), p_41414_.getYRot(), 0.0F, 1.5F, 1.0F);
                p_41413_.addFreshEntity(snowball);
            }
            if (p_41414_ instanceof Player player) {
                player.awardStat(Stats.ITEM_USED.get(this));
                if (!player.getAbilities().instabuild || (player.getAbilities().instabuild && player.isCrouching())) {
                    setFilled(p_41412_, null);
                }
            }

        } else {
            super.releaseUsing(p_41412_, p_41413_, p_41414_, p_41415_);
        }
    }
}
