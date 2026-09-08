package com.terramc.tm.compat.tfc.weapon.entity;

import com.terramc.tm.compat.tfc.TfcEntities;
import com.terramc.tm.config.JsonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.confluence.mod.common.entity.projectile.mana.AbstractManaProjectile;
import org.mesdag.particlestorm.PSGameClient;
import org.mesdag.particlestorm.particle.ParticleEmitter;

/**
 * 辉星投石索的星弹弹幕。
 * <p>
 * 弹道分两段：前 4 秒（80 tick）沿发射方向匀速直飞；此后每 tick 给弹射速度
 * 叠加 0.2 的向上分量，星弹会拖出一条升空弧线。伤害走汇流的 MAGICAL_PROJECTILE
 * 魔法伤害源，命中与射线检测由 {@link AbstractManaProjectile#tick()} 统一调度，
 * 本类只在 {@link #baseTick()} 里补充自身移动逻辑（与汇流 BaseManaStaffProjectileEntity
 * 的做法一致）。
 * <p>
 * 存在时长由 {@code data/tm/config/tfc/weapons.json} 的 {@code projectileLifetime} 配置。
 */
public class RadiantStarProjectile extends AbstractManaProjectile {
    private static final String WEAPONS = "tfc/weapons";

    /** 发射后 4 秒（80 tick）开始向上弹射。 */
    private static final int RISE_TICKS = 10;
    /** 弹射开始后每 tick 向上叠加的速度。 */
    private static final float RISE_VELOCITY = 0.01F;
    /** 复用汇流的坠落之星粒子定义（粒子发射器跟随弹幕，形成星屑拖尾）。 */
    private static final ResourceLocation FALLING_STAR_PARTICLE =
            ResourceLocation.fromNamespaceAndPath("confluence", "falling_star");

    private final int lifetime;
    /** 客户端粒子发射器，仅客户端持有（与汇流 BaseManaStaffProjectileEntity.emitter 同模式）。 */
    private ParticleEmitter emitter;

    public RadiantStarProjectile(EntityType<? extends RadiantStarProjectile> type, Level level) {
        super(type, level);
        // 实体在运行期构造，此时 JSON 数值配置已随数据包加载；
        // 不能用 static final 字段缓存（类加载早于数据包，会永远读到默认值）。
        lifetime = JsonConfig.getInt(WEAPONS, "tfc_radiant_star_sling", "projectileLifetime", 120);
        setNoGravity(true);
    }

    public RadiantStarProjectile(LivingEntity living) {
        this(TfcEntities.RADIANT_STAR_PROJECTILE.get(), living.level());
        // 汇流 1.2.4 二进制的 ManaStaffItem#beforeShoot 只设置伤害/速度/主人/方向，
        // 并不调用 setPos（新版源码才有）；位置必须像 BaseManaStaffProjectileEntity
        // 的 (EntityType, LivingEntity, Level, Variant) 构造器一样在这里设置，
        // 否则弹幕会在世界原点 (0,0,0) 生成，玩家看不到弹幕。
        setOwner(living);
        setPos(living.getX(), living.getEyeY() - 0.1D, living.getZ());
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (tickCount >= RISE_TICKS) {
            Vec3 motion = getDeltaMovement();
            setDeltaMovement(motion.x, motion.y + RISE_VELOCITY, motion.z);
        }
        Vec3 motion = getDeltaMovement();
        setPos(getX() + motion.x, getY() + motion.y, getZ() + motion.z);
        // 与汇流法师弹幕一致：客户端挂 ParticleStorm 发射器形成拖尾，
        // 发射器随实体移动，实体消亡后自行移除（isRemoved 时重建）。
        if (level().isClientSide && (emitter == null || emitter.isRemoved())) {
            this.emitter = new ParticleEmitter(level(), position(), FALLING_STAR_PARTICLE);
            emitter.attachEntity(this);
            PSGameClient.LOADER.addEmitter(emitter, false);
        }
        // JsonConfig 只在服务端随数据包加载（专用服务器下客户端读到的是默认值），
        // 因此以服务端为权威：服务端 discard 后客户端会收到移除包。
        if (tickCount > lifetime && !level().isClientSide) {
            discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        // 客户端侧 Entity.hurt 恒返回 false，因此消亡实际只发生在服务端
        if (doHurtAndKnockback(result.getEntity(), 0.5D, 0.2D)) {
            discard();
        }
    }
}
