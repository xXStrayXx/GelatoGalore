package com.itayfeder.gelato_galore.client.renderers.entities;

import com.itayfeder.gelato_galore.GelatoGalore;
import com.itayfeder.gelato_galore.client.models.entities.IceCreamBallModel;
import com.itayfeder.gelato_galore.entities.projectiles.IceCreamBall;
import com.itayfeder.gelato_galore.reload.FlavorData;
import com.itayfeder.gelato_galore.reload.FlavorDataReloadListener;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.Boat;

import java.awt.*;

public class IceCreamBallRenderer extends EntityRenderer<IceCreamBall> {
    private final IceCreamBallModel model;

    public IceCreamBallRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
        model = new IceCreamBallModel(p_174008_.bakeLayer(IceCreamBallModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getTextureLocation(IceCreamBall p_114482_) {
        return new ResourceLocation(GelatoGalore.MODID, "textures/entity/ice_cream_ball.png");
    }

    @Override
    public void render(IceCreamBall p_113929_, float p_113930_, float p_113931_, PoseStack p_113932_, MultiBufferSource p_113933_, int p_113934_) {
        p_113932_.pushPose();
        p_113932_.mulPose(Axis.YP.rotationDegrees(Mth.lerp(p_113931_, p_113929_.yRotO, p_113929_.getYRot()) - 90.0F));
        p_113932_.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(p_113931_, p_113929_.xRotO, p_113929_.getXRot())));

        VertexConsumer vertexconsumer = p_113933_.getBuffer(model.renderType(new ResourceLocation(GelatoGalore.MODID, "textures/entity/ice_cream_ball.png")));
        FlavorData flavor = FlavorDataReloadListener.INSTANCE.FLAVOR_MAP.get(p_113929_.getFlavor());
        Color color = flavor.getAsColor();
        model.renderToBuffer(p_113932_, vertexconsumer, p_113934_, OverlayTexture.NO_OVERLAY, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1.0F);

        p_113932_.popPose();
        super.render(p_113929_, p_113930_, p_113931_, p_113932_, p_113933_, p_113934_);
    }

}
