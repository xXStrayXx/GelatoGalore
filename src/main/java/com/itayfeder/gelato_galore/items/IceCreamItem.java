package com.itayfeder.gelato_galore.items;

import com.itayfeder.gelato_galore.GelatoGalore;
import com.itayfeder.gelato_galore.GelatoGaloreConfig;
import com.itayfeder.gelato_galore.client.renderers.IceCreamItemRenderer;
import com.itayfeder.gelato_galore.init.ItemInit;
import com.itayfeder.gelato_galore.reload.FlavorData;
import com.itayfeder.gelato_galore.reload.FlavorDataReloadListener;
import com.itayfeder.gelato_galore.toppings.TickableTopping;
import com.itayfeder.gelato_galore.toppings.Topping;
import com.itayfeder.gelato_galore.toppings.Toppings;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class IceCreamItem extends Item {
    public final int scoops;
    public static final int MAX_SCOOPS = 3;

    public IceCreamItem(int scoops, Properties p_41383_) {
        super(p_41383_.food((new FoodProperties.Builder()).nutrition(2 * scoops).saturationMod(0.2F * scoops).build()));
        this.scoops = scoops;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack p_41409_, Level p_41410_, LivingEntity p_41411_) {
        if (!p_41410_.isClientSide) {
            if (GelatoGaloreConfig.ServerConfig.ICECREAM_EFFECT.get()) {
                List<MobEffectInstance> instances = new ArrayList<>();
                for (int i = 0; i < scoops; i++) {
                    MobEffectInstance instance = getFlavor(p_41409_, i).effect.construct();
                    if (instance != null)
                        instances.add(instance);
                }
                instances = getTopping(p_41409_).editAppliedEffects(instances);
                for (MobEffectInstance effectInstance : instances) {
                    p_41411_.addEffect(effectInstance);
                }
            }


        }
        return super.finishUsingItem(p_41409_, p_41410_, p_41411_);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        return stack;
    }

    public void defaultBuild(ItemStack stack) {
        setFlavor(stack, FlavorDataReloadListener.getSidedMap().values().stream().toList().get(0), 0);
        setFlavor(stack, FlavorDataReloadListener.getSidedMap().values().stream().toList().get(1), 1);
        setFlavor(stack, FlavorDataReloadListener.getSidedMap().values().stream().toList().get(2), 2);
        setTopping(stack, Toppings.EMPTY);
    }

    @Override
    public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
        super.inventoryTick(p_41404_, p_41405_, p_41406_, p_41407_, p_41408_);
        if (getTopping(p_41404_) instanceof TickableTopping) {
            ((TickableTopping)getTopping(p_41404_)).tick(p_41404_, p_41405_);
        }
    }

    public void setFlavor(ItemStack p_220011_0_, FlavorData p_220011_1_, int location) {
        CompoundTag compoundnbt = p_220011_0_.getOrCreateTag();
        ResourceLocation[] currentFlavors = new ResourceLocation[scoops];
        ListTag flavorTag = compoundnbt.getList("Flavors", 10);
        if (flavorTag.size() == 0) {
            currentFlavors = new ResourceLocation[scoops];
            for (int i = 0; i < scoops; i++) {
                currentFlavors[i] = FlavorDataReloadListener.getSidedMap().values().stream().toList().get(0).id;
            }
        }
        for (int i = 0; i < flavorTag.size(); i++) {
            CompoundTag tag = flavorTag.getCompound(i);
            currentFlavors[i] = ResourceLocation.tryParse(tag.getString(String.valueOf(i)));
        }
        if (currentFlavors.length > location) {
            currentFlavors[location] = p_220011_1_.id;
        }
        compoundnbt.put("Flavors", toListTag(currentFlavors));
    }

    public ListTag toListTag(ResourceLocation[] resources) {
        ListTag tagList = new ListTag();
        for (int i = 0; i < resources.length; i++) {
            CompoundTag tag = new CompoundTag();
            if (resources[i] != null)
                tag.putString(String.valueOf(i), resources[i].toString());
            else {
                tag.putString(String.valueOf(i),"gelato_galore:chocolate");
            }
            tagList.add(tag);
        }
        return tagList;
    }

    public FlavorData getFlavor(ItemStack p_220012_0_, int location) {
        CompoundTag compoundnbt = p_220012_0_.getOrCreateTag();
        ListTag currentFlavors = compoundnbt.getList("Flavors", 10);
        if (currentFlavors.size() == 0)
            return FlavorDataReloadListener.getSidedMap().values().stream().toList().get(0);
        if (location >= currentFlavors.size())
            return FlavorDataReloadListener.getSidedMap().values().stream().toList().get(0);

        CompoundTag tag = currentFlavors.getCompound(location);
        ResourceLocation relocation = ResourceLocation.tryParse(tag.getString(String.valueOf(location)));
        return relocation != null ? FlavorDataReloadListener.getSidedMap().get(relocation) : FlavorDataReloadListener.getSidedMap().values().stream().toList().get(0);
    }

    public static void setTopping(ItemStack p_220011_0_, Topping topping) {
        CompoundTag compoundnbt = p_220011_0_.getOrCreateTag();
        compoundnbt.put("Topping", topping.createTag());
    }

    public static Topping getTopping(ItemStack stack) {
        CompoundTag compoundnbt = stack.getOrCreateTag();
        return Topping.readTag(compoundnbt.getCompound("Topping"));
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        for (int i = 0; i < scoops; i++) {
            if (getFlavor(p_41421_, i) == null) p_41423_.add(Component.translatable("flavor." + "unknown", new Object[0]).withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
            else p_41423_.add(Component.translatable("flavor." + getFlavor(p_41421_, i).name, new Object[0]).withStyle(Style.EMPTY.withColor(getFlavor(p_41421_, i).color)));
        }
        if (!getTopping(p_41421_).location.equals(Toppings.EMPTY.location)) {
            p_41423_.add(
                    Component.translatable("tooltip.gelato_galore.topping", new Object[0]).withStyle(ChatFormatting.GRAY)
                            .append(Component.literal(": "))
                            .append(getTopping(p_41421_).getText())
            );
        }

        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);

    }

    @Override
    public String getDescriptionId(ItemStack p_41455_) {
        return "item.gelato_galore.ice_cream";
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new IceCreamItemRenderer();
            }
        });
    }
}

