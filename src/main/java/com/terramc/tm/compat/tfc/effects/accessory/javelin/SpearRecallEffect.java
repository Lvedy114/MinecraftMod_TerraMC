package com.terramc.tm.compat.tfc.effects.accessory.javelin;

import com.terramc.tm.accessory.AccessoryEffect;
import com.terramc.tm.compat.tfc.config.TfcConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * 标枪周期性回收效果：每隔固定时间，将玩家附近大范围内的所有标枪强制召回，
 * 并沿路径对生物造成伤害（可配置）。
 * <p>
 * 冷却按玩家 UUID 单独记录（以游戏刻为基准），多名玩家佩戴互不影响。
 * 效果实例在物品注册时只创建一次、被所有佩戴者共享，
 * 因此计时状态绝不能放在实例字段里（否则佩戴者越多冷却越快）。
 */
public class SpearRecallEffect extends AccessoryEffect {
    /** 召回飞行速度（格/刻）。 */
    private static final double RECALL_SPEED = 1.5;

    private final Supplier<Boolean> enabled;
    private final Supplier<Double> recallRange;
    private final Supplier<Double> pathDamage;
    private final Supplier<Integer> recallCooldownTicks;

    /** 每个玩家上次触发回收的游戏刻（UUID -> 游戏刻）。 */
    private final Map<UUID, Long> lastRecallAt = new ConcurrentHashMap<>();

    /** 不带路径伤害的回收效果。 */
    public SpearRecallEffect(Supplier<Boolean> enabled, Supplier<Double> recallRange,
                             Supplier<Integer> recallCooldownTicks) {
        this(enabled, recallRange, recallCooldownTicks, null);
    }

    public SpearRecallEffect(Supplier<Boolean> enabled, Supplier<Double> recallRange,
                             Supplier<Integer> recallCooldownTicks, Supplier<Double> pathDamage) {
        this.enabled = enabled;
        this.recallRange = recallRange;
        this.recallCooldownTicks = recallCooldownTicks;
        this.pathDamage = pathDamage;
    }

    @Override
    public void onCurioTick(SlotContext slotContext, LivingEntity entity, ItemStack stack) {
        // 标枪系效果仅限玩家使用
        if (!(entity instanceof ServerPlayer player)) {
            return;
        }
        if (!enabled.get()) {
            return;
        }

        // 间隔控制：按玩家单独计时，未到时间则跳过
        long now = player.level().getGameTime();
        Long last = lastRecallAt.get(player.getUUID());
        if (last != null && now - last < recallCooldownTicks.get()) {
            return;
        }

        double range = recallRange.get();
        double pathDmg = pathDamage != null ? pathDamage.get() : 0.0;

        // 获取范围内所有属于玩家的标枪（无论是否落地/飞行）
        List<AbstractArrow> javelins = player.level().getEntitiesOfClass(
                AbstractArrow.class,
                player.getBoundingBox().inflate(range),
                a -> a.getOwner() == player && TfcConfig.isSpear(a) && !a.isRemoved());

        boolean recalled = false;
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
            recalled = true;

            // 如果配置了路径伤害，则对召回路径上的生物造成伤害
            if (pathDmg > 0) {
                damageAlongPath(player, javelin, (float) pathDmg);
            }
        }

        // 只有真正触发了召回才进入冷却，没有标枪可召回时下一刻继续尝试
        if (recalled) {
            lastRecallAt.put(player.getUUID(), now);
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
