package com.itayfeder.gelato_galore.init;

import com.itayfeder.gelato_galore.GelatoGalore;
import com.itayfeder.gelato_galore.items.IceCreamItem;
import com.itayfeder.gelato_galore.items.IceCreamSandwichItem;
import com.itayfeder.gelato_galore.items.ScooperItem;
import com.itayfeder.gelato_galore.items.SyrupItem;
import com.itayfeder.gelato_galore.items.api.ToppingItem;
import com.itayfeder.gelato_galore.reload.FlavorData;
import com.itayfeder.gelato_galore.reload.FlavorDataReloadListener;
import com.itayfeder.gelato_galore.toppings.Toppings;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.Collectors;

import static com.itayfeder.gelato_galore.items.IceCreamItem.setTopping;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, GelatoGalore.MODID);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GelatoGalore.MODID);

    public static final RegistryObject<Item> CARAMEL = ITEMS.register("caramel", () -> new Item((new Item.Properties()).food((new FoodProperties.Builder()).nutrition(2).saturationMod(0.3F).build()).stacksTo(64)));
    public static final RegistryObject<Item> VANILLA_PODS = ITEMS.register("vanilla_pods", () -> new ItemNameBlockItem(BlockInit.VANILLA_VINE.get(), (new Item.Properties()).stacksTo(64)));
    public static final RegistryObject<Item> DRIED_VANILLA_PODS = ITEMS.register("dried_vanilla_pods", () -> new Item((new Item.Properties()).stacksTo(64)));

    public static final RegistryObject<Item> SYRUP = ITEMS.register("syrup", () -> new SyrupItem((new Item.Properties()).stacksTo(16)));
    public static final RegistryObject<Item> ICE_CREAM_CONE = ITEMS.register("ice_cream_cone", () -> new Item((new Item.Properties()).stacksTo(64)));

    public static final RegistryObject<Item> ICE_CREAM_ONE = ITEMS.register("ice_cream_one", () -> new IceCreamItem(1, (new Item.Properties()).stacksTo(1)));
    public static final RegistryObject<Item> ICE_CREAM_TWO = ITEMS.register("ice_cream_two", () -> new IceCreamItem(2, (new Item.Properties()).stacksTo(1)));
    public static final RegistryObject<Item> ICE_CREAM_THREE = ITEMS.register("ice_cream_three", () -> new IceCreamItem(3, (new Item.Properties()).stacksTo(1)));

    public static final RegistryObject<Item> ICE_CREAM_SANDWICH = ITEMS.register("ice_cream_sandwich", () -> new IceCreamSandwichItem((new Item.Properties()).stacksTo(1).durability(4)));

    public static final RegistryObject<Item> RAINBOW_SPRINKLES = ITEMS.register("rainbow_sprinkles", () -> new ToppingItem((new Item.Properties()).durability(8), Toppings.RAINBOW_SPRINKLES, true));
    public static final RegistryObject<Item> COOKIE_CHIPS = ITEMS.register("cookie_chips", () -> new ToppingItem((new Item.Properties()).durability(8), Toppings.COOKIE_CHIPS, true));
    public static final RegistryObject<Item> SCOOPER = ITEMS.register("scooper", () -> new ScooperItem((new Item.Properties()).stacksTo(1)));


    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("gelato_galore", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.gelato_galore"))
            .icon(Items.ICE::getDefaultInstance)
            .displayItems((parameters, output) -> {
                output.accept(CARAMEL.get());
                output.accept(VANILLA_PODS.get());
                output.accept(DRIED_VANILLA_PODS.get());

                for (FlavorData data : FlavorDataReloadListener.getSidedMap().values()) {
                    ItemStack is = new ItemStack(SYRUP.get(), 1);
                    SyrupItem.setFilled(is, data.id);
                    output.accept(is);
                }

                output.accept(ICE_CREAM_CONE.get());

                for (FlavorData data : FlavorDataReloadListener.getSidedMap().values()) {
                    ItemStack is = new ItemStack(ICE_CREAM_ONE.get(), 1);
                    ((IceCreamItem)ICE_CREAM_ONE.get()).setFlavor(is, data, 0);
                    setTopping(is, Toppings.EMPTY);
                    output.accept(is);
                }

                output.accept(RAINBOW_SPRINKLES.get());
                output.accept(COOKIE_CHIPS.get());
                output.accept(SCOOPER.get());

                output.accept(ICE_CREAM_SANDWICH.get().getDefaultInstance());

            }).build());


    public static final RegistryObject<CreativeModeTab> TAB_ALL = CREATIVE_MODE_TABS.register("gelato_galore_all", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.gelato_galore_all"))
            .icon(ICE_CREAM_ONE.get()::getDefaultInstance)
            .displayItems((parameters, output) -> {
                for (FlavorData data0 : FlavorDataReloadListener.getSidedMap().values()) {
                    for (FlavorData data1 : FlavorDataReloadListener.getSidedMap().values()) {
                        for (FlavorData data2 : FlavorDataReloadListener.getSidedMap().values()) {
                            ItemStack is = new ItemStack(((IceCreamItem)ICE_CREAM_THREE.get()), 1);
                            ((IceCreamItem)ICE_CREAM_THREE.get()).setFlavor(is, data0, 0);
                            ((IceCreamItem)ICE_CREAM_THREE.get()).setFlavor(is, data1, 1);
                            ((IceCreamItem)ICE_CREAM_THREE.get()).setFlavor(is, data2, 2);
                            setTopping(is, Toppings.EMPTY);
                            output.accept(is);

                        }
                    }
                }
            }).build());
}
