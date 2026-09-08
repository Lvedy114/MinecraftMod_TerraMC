package com.terramc.tm.compat.tfc.weapon.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.confluence.mod.common.component.SwordProjectileComponent;
import org.confluence.mod.common.entity.projectile.sword.SwordProjectile;

import java.util.HashSet;
import java.util.Set;

/**
 * 石头棒子的翻滚弹幕。
 * <p>
 * 仿照汇流来世 {@code ForwardSwordProjectile}：伤害、速度、冷却、重力、寿命等全部
 * 来自 {@code SwordProjectileComponent}（即 tooltip 上的剑气三属性），
 * 由 {@code BaseSwordItem#genProjectile} 统一注入；本类只负责直线位移与穿透去重。
 */
public class StoneClubProjectile extends SwordProjectile {

    /** 穿透弹幕：每个目标只结算一次伤害。 */
    private final Set<Integer> hitEntities = new HashSet<>();

    public StoneClubProjectile(EntityType<? extends StoneClubProjectile> type, Level level) {
        super(type, level);
        // 翻滚石头可穿透多个目标；基类 doHurt 中 --hitCount <= 0 才丢弃
        hitCount = Integer.MAX_VALUE;
    }

    @Override
    public void tick() {
        // 基类处理寿命（existTicks/lifetime）、重力与 AABB 碰撞攻击
        // （伤害 = damageFactor × 攻击者攻击力属性，由 genProjectile 注入）
        super.tick();
        Vec3 motion = getDeltaMovement();
        setPos(getX() + motion.x, getY() + motion.y, getZ() + motion.z);
    }

    @Override
    protected boolean doHurt(Entity target) {
        // 穿透去重必须放在 doHurt 而不是 canHitEntity：
        // 原版 AbstractHurtingProjectile.tick 的射线检测也会调用 canHitEntity，
        // 若在 canHitEntity 里做去重副作用，目标会被提前记入已命中集合，
        // 随后基类 doCollisionAttack 的过滤永远失败，弹幕将不造成任何伤害。
        if (!hitEntities.add(target.getId())) {
            return false;
        }
        return super.doHurt(target);
    }
}
