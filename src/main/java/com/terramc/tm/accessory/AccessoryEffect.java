package com.terramc.tm.accessory;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.ArmorHurtEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

/**
 * 饰品效果基类。
 * <p>
 * 一个饰品可挂载多个效果，每个效果是「单一职责」的自包含行为单元。
 * 子类只需覆写需要的钩子；数值一律从配置读取（见 {@code com.terramc.tm.config}），避免硬编码。
 * <p>
 * 所有钩子由 {@link AccessoryEvents} 统一分发（装备/卸下由 {@link AccessoryItem} 委托）。
 * 新增事件：在 {@link AccessoryEvents} 中增加监听方法并在 {@code register()} 中显式注册，
 * 同时在此类增加对应钩子即可。
 */
public abstract class AccessoryEffect {
    /** 投掷物命中目标时触发（由 {@code LivingIncomingDamageEvent} 分发）。 */
    public void onThrownProjectileHit(LivingIncomingDamageEvent event, ServerPlayer owner, Projectile projectile) {
    }

    /** 投掷物实体加入世界时触发（由 {@code EntityJoinLevelEvent} 分发）。 */
    public void onProjectileSpawn(EntityJoinLevelEvent event, ServerPlayer owner, Projectile projectile) {
    }

    /** 玩家每刻触发（由 {@code PlayerTickEvent.Post} 分发，仅服务端）。 */
    public void onPlayerTick(ServerPlayer player) {
    }

    /** 饰品被装备时触发（由 Curios 的 {@code onEquip} 委托）。 */
    public void onEquip(ServerPlayer player, ItemStack stack) {
    }

    /** 饰品被卸下时触发（由 Curios 的 {@code onUnequip} 委托）。 */
    public void onUnequip(ServerPlayer player) {
    }

    /** 穿戴者受到伤害后触发（由 {@code LivingDamageEvent.Post} 分发）。 */
    public void onDamaged(LivingDamageEvent.Post event, ServerPlayer player) {
    }

    /** 穿戴者的护甲即将消耗耐久时触发（由 {@code ArmorHurtEvent} 分发）。 */
    public void onArmorHurt(ArmorHurtEvent event, ServerPlayer player) {
    }
}