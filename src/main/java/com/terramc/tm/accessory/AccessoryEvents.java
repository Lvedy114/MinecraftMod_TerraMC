package com.terramc.tm.accessory;

import com.terramc.tm.TerraMC;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

/**
 * 饰品效果事件分发器。
 * <p>
 * 这里是「效果钩子」与「游戏事件」之间的桥梁：监听相关的 Forge 事件，找出事件对应的
 * 投掷物与其主人（玩家），再遍历该玩家装备的饰品效果，逐一调用对应钩子。
 * <p>
 * 新增需要响应的事件时，在此类中新增一个 {@code @SubscribeEvent} 方法即可。
 */
@EventBusSubscriber(modid = TerraMC.MODID)
public final class AccessoryEvents {
    private AccessoryEvents() {
    }

    /**
     * 投掷物命中实体造成伤害时触发。
     * 用于分发 {@link AccessoryEffect#onThrownProjectileHit}。
     */
    @SubscribeEvent
    public static void onLivingHurt(LivingIncomingDamageEvent event) {
        Entity direct = event.getSource().getDirectEntity();
        if (!(direct instanceof Projectile projectile)) {
            return;
        }
        if (!(projectile.getOwner() instanceof ServerPlayer player)) {
            return;
        }
        for (AccessoryDefinition definition : AccessoryManager.equipped(player)) {
            for (AccessoryEffect effect : definition.effects()) {
                effect.onThrownProjectileHit(event, player, projectile);
            }
        }
    }

    /**
     * 实体（含投掷物）加入世界时触发。
     * 用于分发 {@link AccessoryEffect#onProjectileSpawn}。
     */
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Projectile projectile)) {
            return;
        }
        if (!(projectile.getOwner() instanceof ServerPlayer player)) {
            return;
        }
        for (AccessoryDefinition definition : AccessoryManager.equipped(player)) {
            for (AccessoryEffect effect : definition.effects()) {
                effect.onProjectileSpawn(event, player, projectile);
            }
        }
    }
}
