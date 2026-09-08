package com.terramc.tm.compat.tfc.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.terramc.tm.compat.tfc.weapon.entity.RadiantStarProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

/**
 * 辉星弹幕渲染器。
 * <p>
 * 参考汇流来世 FallingStarRenderer 的做法：用 {@link RenderType#lightning()} 绘制
 * 白黄渐变的发光三角形，无需纹理；六根星芒沿 ±X/±Y/±Z 分布并整体自转。
 */
public final class RadiantStarProjectileRenderer extends EntityRenderer<RadiantStarProjectile> {
    /** 星芒尖端到中心的距离。 */
    private static final float LENGTH = 0.45F;
    /** 星芒底部半宽。 */
    private static final float WIDTH = 0.14F;
    /** 自转角速度（度/tick）。 */
    private static final float SPIN_SPEED = 6.0F;

    public RadiantStarProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(RadiantStarProjectile entity) {
        // lightning 渲染类型不采样纹理，返回任意有效路径即可
        return ResourceLocation.withDefaultNamespace("textures/entity/experience_orb.png");
    }

    @Override
    protected int getBlockLightLevel(RadiantStarProjectile entity, BlockPos pos) {
        return 15;
    }

    @Override
    public void render(RadiantStarProjectile entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees((entity.tickCount + partialTick) * SPIN_SPEED));

        VertexConsumer consumer = buffer.getBuffer(RenderType.lightning());
        // ±Y 方向两根 + 绕 X 轴再放四根，构成 3D 六芒星
        for (int i = 0; i < 4; i++) {
            spike(consumer, poseStack);
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        }

        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    /**
     * 沿局部 +Y 方向画一根星芒（中心白 → 尖端黄的放射面片），再由外层旋转变换到六个朝向。
     * <p>
     * 注意：{@link RenderType#lightning()} 是 <b>QUADS</b> 绘制模式，每个图元必须恰好
     * 4 个顶点（汇流 FallingStarRenderer 同此模式）；只发 3 个顶点会把相邻星芒的
     * 顶点错误拼接成畸形面片。
     */
    private static void spike(VertexConsumer consumer, PoseStack poseStack) {
        Matrix4f matrix = poseStack.last().pose();
        // 星芒底部亮白，尖端淡黄消隐
        consumer.addVertex(matrix, 0.0F, 0.0F, 0.0F).setColor(255, 255, 230, 220);
        consumer.addVertex(matrix, -WIDTH, 0.05F, 0.0F).setColor(255, 255, 230, 220);
        consumer.addVertex(matrix, 0.0F, LENGTH, 0.0F).setColor(255, 220, 90, 0);
        consumer.addVertex(matrix, WIDTH, 0.05F, 0.0F).setColor(255, 255, 230, 220);
    }
}
