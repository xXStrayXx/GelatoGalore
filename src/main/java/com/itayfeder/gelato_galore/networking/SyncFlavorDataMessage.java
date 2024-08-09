package com.itayfeder.gelato_galore.networking;

import com.google.common.collect.HashBiMap;
import com.itayfeder.gelato_galore.reload.FlavorData;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.UnboundedMapCodec;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.Map;
import java.util.function.Supplier;

public class SyncFlavorDataMessage {
    public static final Map<ResourceLocation, FlavorData> CLIENT_FLAVORS_MAP = HashBiMap.create();
    private static final Codec<Map<ResourceLocation, FlavorData>> CODEC = Codec.unboundedMap(ResourceLocation.CODEC, FlavorData.CODEC);

    private final Map<ResourceLocation, FlavorData> flavors;

    public SyncFlavorDataMessage(FriendlyByteBuf buf) {
        this.flavors = buf.readJsonWithCodec(CODEC);
    }

    public SyncFlavorDataMessage(Map<ResourceLocation, FlavorData> flavors) {
        this.flavors = flavors;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeJsonWithCodec(CODEC, this.flavors);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            CLIENT_FLAVORS_MAP.clear();
            CLIENT_FLAVORS_MAP.putAll(flavors);
        });

        context.setPacketHandled(true);
        return true;
    }
}
