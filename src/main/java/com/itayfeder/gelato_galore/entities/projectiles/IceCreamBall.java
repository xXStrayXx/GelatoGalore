package com.itayfeder.gelato_galore.entities.projectiles;

import com.itayfeder.gelato_galore.init.EntityTypeInit;
import com.itayfeder.gelato_galore.reload.FlavorData;
import com.itayfeder.gelato_galore.reload.FlavorDataReloadListener;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class IceCreamBall extends ThrowableProjectile {
    private static final EntityDataAccessor<String> DATA_FLAVOR = SynchedEntityData.defineId(IceCreamBall.class, EntityDataSerializers.STRING);

    public IceCreamBall(EntityType<? extends IceCreamBall> p_37466_, Level p_37467_) {
        super(EntityTypeInit.ICE_CREAM_BALL.get(), p_37467_);
    }

    public IceCreamBall(EntityType<? extends IceCreamBall> p_37456_, double p_37457_, double p_37458_, double p_37459_, Level p_37460_) {
        super(EntityTypeInit.ICE_CREAM_BALL.get(), p_37460_);
        this.setPos(p_37457_, p_37458_, p_37459_);
    }

    public IceCreamBall(LivingEntity p_37463_, Level p_37464_) {
        super(EntityTypeInit.ICE_CREAM_BALL.get(), p_37463_, p_37464_);
        this.setOwner(p_37463_);
    }


    private ParticleOptions getParticle() {
        FlavorData flavor = FlavorDataReloadListener.INSTANCE.FLAVOR_MAP.get(getFlavor());
        return new DustParticleOptions(new Vector3f(flavor.getAsColor().getColorComponents(null)), 1f);
    }

    public void handleEntityEvent(byte p_37402_) {
        if (p_37402_ == 3) {
            ParticleOptions particleoptions = this.getParticle();

            for(int i = 0; i < 8; ++i) {
                this.level().addParticle(particleoptions, this.getRandomX(0.3), this.getRandomY(0.3) + 0.15, this.getRandomZ(0.3), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    @Override
    public void tick() {
        super.tick();


        boolean flag = this.isNoGravity();
        Vec3 vec3 = this.getDeltaMovement();
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double d0 = vec3.horizontalDistance();
            this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
            this.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }

        if (flag) {
            vec3 = this.getDeltaMovement();
            double d5 = vec3.x;
            double d6 = vec3.y;
            double d1 = vec3.z;

            double d4 = vec3.horizontalDistance();
            if (flag) {
                this.setYRot((float)(Mth.atan2(-d5, -d1) * (double)(180F / (float)Math.PI)));
            } else {
                this.setYRot((float)(Mth.atan2(d5, d1) * (double)(180F / (float)Math.PI)));
            }

            this.setXRot((float)(Mth.atan2(d6, d4) * (double)(180F / (float)Math.PI)));
            this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
            this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
        } else {
            vec3 = this.getDeltaMovement();
            double d5 = vec3.x;
            double d6 = vec3.y;
            double d1 = vec3.z;

            if (this.level().isClientSide) {
                ParticleOptions particleoptions = this.getParticle();

                for(int i = 0; i < 8; ++i) {
                    this.level().addParticle(particleoptions, this.getRandomX(0.3) - (d5 * 0.8), this.getRandomY(0.3) + 0.15 - (d6 * 0.8), this.getRandomZ(0.3) - (d1 * 0.8), 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    public double getRandomY(double p_20263_) {
        return this.getY((2.0D * this.random.nextDouble() - 1.0D) * p_20263_);
    }

    @Override
    protected float getEyeHeight(Pose p_19976_, EntityDimensions p_19977_) {
        return p_19977_.height * 0.5f;
    }

    @Override
    protected void onHitBlock(BlockHitResult p_37258_) {
        super.onHitBlock(p_37258_);
        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult p_37259_) {
        super.onHitEntity(p_37259_);
        p_37259_.getEntity().hurt(this.damageSources().freeze(), 3);
        p_37259_.getEntity().setTicksFrozen(300);
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(DATA_FLAVOR, FlavorDataReloadListener.INSTANCE.FLAVOR_MAP.keySet().stream().toList().get(0).toString());
    }

    public void setFlavor(ResourceLocation p_36768_) {
        this.entityData.set(DATA_FLAVOR, p_36768_.toString());
    }

    public ResourceLocation getFlavor() {
        return ResourceLocation.tryParse(this.entityData.get(DATA_FLAVOR));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_37265_) {
        super.addAdditionalSaveData(p_37265_);
        p_37265_.putString("Flavor", getFlavor().toString());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_37262_) {
        super.readAdditionalSaveData(p_37262_);
        setFlavor(ResourceLocation.tryParse(p_37262_.getString("Flavor")));

    }
}
