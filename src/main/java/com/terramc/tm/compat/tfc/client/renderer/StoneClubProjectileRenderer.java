package com.terramc.tm.compat.tfc.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.terramc.tm.compat.tfc.weapon.entity.StoneClubProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

/**
 * 石头棒子弹幕渲染器。
 * <p>
 * 旋转顺序参考汇流来世 ForwardProjRenderer（冰雪刀弹幕）：
 * 初始方向建立固定的 Yaw/Pitch 局部坐标系，然后只绕局部 Z 轴持续滚转。
 */
public final class StoneClubProjectileRenderer extends EntityRenderer<StoneClubProjectile> {
    private static final float SCALE = 0.28F;
    private static final float ROLL_SPEED = 0.56F;

    private final BlockRenderDispatcher dispatcher;

    public StoneClubProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        dispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public ResourceLocation getTextureLocation(StoneClubProjectile entity) {
        return ResourceLocation.withDefaultNamespace("textures/block/cobblestone.png");
    }

    @Override
    public void render(StoneClubProjectile entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0F, 0.1F, 0F);
        // 与汇流 ForwardProjRenderer 相同：直接用当前速度方向建立局部坐标系
        Vec3 direction = entity.getDeltaMovement();
        if (direction.lengthSqr() > 1.0E-7D) {
            float yaw = (float) Math.atan2(direction.z, direction.x);
            poseStack.mulPose(Axis.YN.rotation(yaw + Mth.HALF_PI));

            float pitch = -(float) Math.atan2(direction.y, direction.horizontalDistance());
            poseStack.mulPose(Axis.XN.rotation(pitch));
        }

        float pitch = (entity.tickCount + partialTick) * ROLL_SPEED;
        poseStack.mulPose(Axis.XN.rotation(pitch));

        poseStack.scale(SCALE, SCALE, SCALE);
        // 将方块几何中心移到旋转原点，保证是自转而不是绕角点公转。
        poseStack.translate(-0.5F, -0.5D, -0.5F);
        dispatcher.renderSingleBlock(
                Blocks.COBBLESTONE.defaultBlockState(),
                poseStack,
                buffer,
                packedLight,
                OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }
}
