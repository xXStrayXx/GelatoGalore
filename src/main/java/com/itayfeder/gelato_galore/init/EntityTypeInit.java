package com.itayfeder.gelato_galore.init;

import com.itayfeder.gelato_galore.GelatoGalore;
import com.itayfeder.gelato_galore.entities.projectiles.IceCreamBall;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityTypeInit {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, GelatoGalore.MODID);

    public static final RegistryObject<EntityType<IceCreamBall>> ICE_CREAM_BALL = ENTITY_TYPES.register("ice_cream_ball",
            () -> EntityType.Builder.<IceCreamBall>of(IceCreamBall::new, MobCategory.MISC)
                    .sized(0.375F, 0.375F).clientTrackingRange(4).updateInterval(20)
                    .build(new ResourceLocation(GelatoGalore.MODID, "ice_cream_ball").toString()));
}
