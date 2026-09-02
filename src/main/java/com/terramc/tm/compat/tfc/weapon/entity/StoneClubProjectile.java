package com.terramc.tm.compat.tfc.weapon.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.confluence.mod.common.entity.projectile.sword.SwordProjectile;

import java.util.HashSet;
import java.util.Set;

/**
 * 石头棒子的翻滚弹幕。
 * <p>
 * 发射时把初始速度方向写入 SwordProjectile 自带的 DATA_DIRECTION 同步字段。
 * 该方向只用于建立固定的渲染局部坐标系；弹幕飞行期间仅累加局部 Z 轴滚转角。
 */
public class StoneClubProjectile extends SwordProjectile {
    private static final int MAX_LIFETIME = 50;
    private static final float DAMAGE = 2.0F;

    private final Set<Integer> hitEntities = new HashSet<>();

    public StoneClubProjectile(EntityType<? extends StoneClubProjectile> type, Level level) {
        super(type, level);
        lifetime = MAX_LIFETIME;
        hitCount = Integer.MAX_VALUE;
        setNoGravity(true);
    }

    public void launch(LivingEntity owner, Vec3 position, Vec3 velocity) {
        Vec3 initialDirection = velocity.normalize();

        setOwner(owner);
        setPos(position);
        setDeltaMovement(velocity);
        baseAttackDamage = DAMAGE;
        attackDamageFactor = 1.0F;

        // 复用汇流来世 SwordProjectile 的同步方向字段，避免另存 X/Y 欧拉角。
        direction = initialDirection;
        entityData.set(DATA_DIRECTION, initialDirection.toVector3f());
    }

    @Override
    public void tick() {
        if (!level().isClientSide && tickCount >= MAX_LIFETIME) {
            discard();
            return;
        }

        Vec3 movement = getDeltaMovement();
        Vec3 end = position().add(movement);
        AABB scan = getBoundingBox().expandTowards(movement).inflate(0.18D);

        if (!level().isClientSide) {
            for (Entity entity : level().getEntities(this, scan,
                    candidate -> candidate instanceof LivingEntity living
                            && living.isAlive()
                            && candidate != getOwner()
                            && !hitEntities.contains(candidate.getId()))) {
                if (entity instanceof LivingEntity living) {
                    hitEntities.add(entity.getId());
                    doHurt(living);
                }
            }
        }

        setPos(end);
        // 本类不调用 SwordProjectile.tick()，因此客户端与服务端都要自行推进视觉年龄。
        tickCount++;
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        return target instanceof LivingEntity living
                && living.isAlive()
                && target != getOwner()
                && !hitEntities.contains(target.getId());
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (!level().isClientSide
                && result.getEntity() instanceof LivingEntity living
                && canHitEntity(living)) {
            hitEntities.add(living.getId());
            doHurt(living);
        }
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0D;
    }

    /** 发射时固定并同步的局部前向轴。 */
    public Vec3 getInitialDirection() {
        return direction.lengthSqr() > 1.0E-7D ? direction.normalize() : getDeltaMovement().normalize();
    }
}
