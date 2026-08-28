package com.terramc.tm.compat.tfc.effects.javelin;

import com.terramc.tm.accessory.AccessoryEffect;
import com.terramc.tm.compat.tfc.config.TfcConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

/**
 * 标枪周期性回收效果：每隔固定时间，将玩家附近大范围内的所有标枪强制召回，
 * 并沿路径对生物造成伤害（可配置）。
 */
public class SpearRecallEffect extends AccessoryEffect {
    /** 召回飞行速度（格/刻）。 */
    private static final double RECALL_SPEED = 1.5;

    private final ModConfigSpec.BooleanValue enabled;
    private final ModConfigSpec.DoubleValue recallRange;
    private final ModConfigSpec.DoubleValue pathDamage;

    // 计时器，记录距离下次回收的剩余刻数
    private int cooldown = 0;

    /** 不带路径伤害的回收效果（仅用于兼容旧构造，但建议使用完整构造）。 */
    public SpearRecallEffect(ModConfigSpec.BooleanValue enabled, ModConfigSpec.DoubleValue recallRange) {
        this(enabled, recallRange, null);
    }

    public SpearRecallEffect(ModConfigSpec.BooleanValue enabled, ModConfigSpec.DoubleValue recallRange,
                             ModConfigSpec.DoubleValue pathDamage) {
        this.enabled = enabled;
        this.recallRange = recallRange;
        this.pathDamage = pathDamage;
    }

    @Override
    public void onPlayerTick(ServerPlayer player) {
        if (!enabled.get()) {
            return;
        }

        // 间隔控制：未到时间则跳过
        if (--cooldown > 0) {
            return;
        }
        // 重置冷却（如果配置未提供，默认 20 刻 = 1 秒）
        cooldown = 200;

        double range = recallRange.get();
        double pathDmg = pathDamage != null ? pathDamage.get() : 0.0;

        // 获取范围内所有属于玩家的标枪（无论是否落地/飞行）
        List<AbstractArrow> javelins = player.level().getEntitiesOfClass(
                AbstractArrow.class,
                player.getBoundingBox().inflate(range),
                a -> a.getOwner() == player && TfcConfig.isSpear(a) && !a.isRemoved());

        for (AbstractArrow javelin : javelins) {
            Vec3 toPlayer = player.getEyePosition().subtract(javelin.position());
            double dist = toPlayer.length();

            // 如果已经足够近，交给原版拾取机制（但不主动拉回）
            if (dist <= 1.0) {
                continue;
            }

            // 强制召回：开启无物理，设置速度指向玩家
            javelin.setNoPhysics(true);
            javelin.setDeltaMovement(toPlayer.normalize().scale(RECALL_SPEED));

            // 如果配置了路径伤害，则对召回路径上的生物造成伤害
            if (pathDmg > 0) {
                damageAlongPath(player, javelin, (float) pathDmg);
            }
        }
    }

    private void damageAlongPath(ServerPlayer player, AbstractArrow javelin, float damage) {
        for (LivingEntity entity : javelin.level().getEntitiesOfClass(LivingEntity.class,
                javelin.getBoundingBox().inflate(3),
                e -> e != player && e.isAlive())) {
            entity.hurt(player.damageSources().thrown(javelin, player), damage);
        }
    }
}