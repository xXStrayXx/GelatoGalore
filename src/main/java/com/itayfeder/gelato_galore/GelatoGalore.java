package com.itayfeder.gelato_galore;

import com.itayfeder.gelato_galore.data.GelatoRecipesGen;
import com.itayfeder.gelato_galore.data.advancements.GelatoGaloreAdvancements;
import com.itayfeder.gelato_galore.data.tags.GelatoBlockTagsGen;
import com.itayfeder.gelato_galore.data.tags.GelatoItemTagsGen;
import com.itayfeder.gelato_galore.data.tags.GelatoPoiTypeTagsGen;
import com.itayfeder.gelato_galore.init.*;
import com.itayfeder.gelato_galore.init.villager.PoiTypeInit;
import com.itayfeder.gelato_galore.init.villager.ProfessionInit;
import com.itayfeder.gelato_galore.mixin.PoiTypeMixin;
import com.itayfeder.gelato_galore.networking.SyncFlavorDataMessage;
import com.itayfeder.gelato_galore.toppings.Toppings;
import com.itayfeder.gelato_galore.utils.CauldronInter;
import com.mojang.logging.LogUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(GelatoGalore.MODID)
public class GelatoGalore
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "gelato_galore";
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.ChannelBuilder.named(
            new ResourceLocation(MODID, MODID))
            .serverAcceptedVersions((status) -> true)
            .clientAcceptedVersions((status) -> true)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    private static int messageID = 0;

    public GelatoGalore()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        ItemInit.ITEMS.register(modEventBus);
        BlockInit.BLOCKS.register(modEventBus);
        BlockEntityTypeInit.BLOCK_ENTITY_TYPES.register(modEventBus);
        ItemInit.CREATIVE_MODE_TABS.register(modEventBus);
        EntityTypeInit.ENTITY_TYPES.register(modEventBus);

        RecipeInit.RECIPE_SERIALIZERS.register(modEventBus);
        ProfessionInit.VILLAGER_PROFESSIONS.register(modEventBus);
        PoiTypeInit.POI_TYPES.register(modEventBus);


        GelatoGaloreConfig.register();

        Toppings.Init();

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::serverSetup);
        modEventBus.addListener(this::dataSetup);
    }

    private void dataSetup(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> registries = event.getLookupProvider();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        boolean includeServer = event.includeServer();

        GelatoBlockTagsGen blockTagsProvider = (GelatoBlockTagsGen) generator.addProvider(event.includeServer(), new GelatoBlockTagsGen(output, registries, fileHelper));
        generator.addProvider(event.includeServer(), new GelatoItemTagsGen(output, registries, blockTagsProvider.contentsGetter(), fileHelper));
        generator.addProvider(event.includeServer(), new GelatoPoiTypeTagsGen(output, registries));

        generator.addProvider(includeServer, new GelatoRecipesGen(output));
        generator.addProvider(includeServer, new ForgeAdvancementProvider(output, registries, fileHelper, List.of(new GelatoGaloreAdvancements())));
//        dataGenerator.addProvider(includeServer, new GelatoLootTableGen(dataGenerator));
//
//        dataGenerator.addProvider(includeServer, new GelatoAdvancementGen(dataGenerator, existingFileHelper));

    }

    private void serverSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ProfessionInit.fillTradeData();
//            PoiTypeMixin.invokeGetBlockStates(BlockInit.ICE_CREAM_CAULDRON.get()).forEach((state) -> PoiTypeMixin.getTypeByState().put(state, BuiltInRegistries.POINT_OF_INTEREST_TYPE.getHolder(ResourceKey.create(Registry.POINT_OF_INTEREST_TYPE_REGISTRY, new ResourceLocation(MODID, "ice_cream_cauldron"))).get()));
        });
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        GelatoGalore.addMessage(SyncFlavorDataMessage.class, SyncFlavorDataMessage::toBytes, SyncFlavorDataMessage::new, SyncFlavorDataMessage::handle);

        CauldronInter.register();
    }

    public static <M> void addMessage(Class<M> messageType, BiConsumer<M, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, M> decoder,
                                      BiConsumer<M, Supplier<NetworkEvent.Context>> messageConsumer) {
        PACKET_HANDLER.messageBuilder(messageType, messageID, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(encoder).decoder(decoder).consumerMainThread(messageConsumer).add();
        System.out.println("REGISTERED MESSAGE: " + messageType.getName());
        messageID++;
    }
}
