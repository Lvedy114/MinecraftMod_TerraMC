package com.terramc.tm.accessory;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

/**
 * 饰品效果基类。
 * <p>
 * 一个饰品可以挂载多个效果，每个效果是「单一职责」的自包含行为单元。
 * 效果的具体逻辑通过覆写下面的钩子方法实现；不需要的钩子保持默认空实现即可。
 * <p>
 * 所有钩子都由 {@link AccessoryEvents} 统一分发：当满足条件的游戏事件发生时，
 * 会遍历「持有该饰品」的玩家所对应的效果，并调用相应钩子。
 * <p>
 * 扩展方式：
 * <ol>
 *   <li>继承本类，覆写需要的钩子（如需新事件，请在 {@link AccessoryEvents} 中新增对应的分发逻辑）。</li>
 *   <li>数值一律从配置中读取（见 {@code com.terramc.tm.config}），避免硬编码。</li>
 * </ol>
 */
public abstract class AccessoryEffect {
    private final String id;

    protected AccessoryEffect(String id) {
        this.id = id;
    }

    /** 效果唯一 ID（在同一饰品内唯一），例如 {@code thrown_damage_bonus}。 */
    public String id() {
        return id;
    }

    /**
     * 投掷物命中目标时触发（由 {@code LivingIncomingDamageEvent} 分发）。
     *
     * @param event     伤害事件
     * @param owner     投掷该投掷物的玩家（已确认持有对应饰品）
     * @param projectile 命中的投掷物实体
     */
    public void onThrownProjectileHit(LivingIncomingDamageEvent event, ServerPlayer owner, Projectile projectile) {
    }

    /**
     * 投掷物实体加入世界时触发（由 {@code EntityJoinLevelEvent} 分发）。
     *
     * @param event     加入世界事件
     * @param owner     投掷该投掷物的玩家（已确认持有对应饰品）
     * @param projectile 刚生成的投掷物实体
     */
    public void onProjectileSpawn(EntityJoinLevelEvent event, ServerPlayer owner, Projectile projectile) {
    }
}
