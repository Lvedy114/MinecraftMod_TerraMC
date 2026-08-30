package com.terramc.tm.accessory;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.ArmorHurtEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import top.theillusivec4.curios.api.SlotContext;

/**
 * 饰品效果基类。
 * <p>
 * 一个饰品可挂载多个效果，每个效果是「单一职责」的自包含行为单元。
 * 子类只需覆写需要的钩子；数值一律从配置读取（见 {@code com.terramc.tm.config}），避免硬编码。
 * <p>
 * 除两个标枪系投掷物钩子（仅玩家所有者会触发）外，其余钩子的对象是<b>任意服务端
 * {@link LivingEntity} 穿戴者</b>——Curios 为所有生物提供饰品栏，饰品不限于玩家使用；
 * 需要玩家专属逻辑时在效果内部自行判定 {@code entity instanceof ServerPlayer}。
 * <p>
 * 所有钩子由 {@link AccessoryEvents} 与 {@link AccessoryItem} 统一分发。
 * 新增事件：在 {@link AccessoryEvents} 中增加监听方法并在 {@code register()} 中显式注册，
 * 同时在此类增加对应钩子即可。
 */
public abstract class AccessoryEffect {
    /** 投掷物命中目标时触发，仅限玩家所有者（标枪系效果专用，由 {@code LivingIncomingDamageEvent} 分发）。 */
    public void onThrownProjectileHit(LivingIncomingDamageEvent event, ServerPlayer owner, Projectile projectile) {
    }

    /** 投掷物实体加入世界时触发，仅限玩家所有者（标枪系效果专用，由 {@code EntityJoinLevelEvent} 分发）。 */
    public void onProjectileSpawn(EntityJoinLevelEvent event, ServerPlayer owner, Projectile projectile) {
    }

    /** 饰品在配饰栏位中的每刻回调（由 Curios 的 {@code curioTick} 委托，仅服务端、仅真实配饰槽位）。 */
    public void onCurioTick(SlotContext slotContext, LivingEntity entity, ItemStack stack) {
    }

    /** 饰品被装备时触发（由 Curios 的 {@code onEquip} 委托，仅服务端）。 */
    public void onEquip(LivingEntity entity, ItemStack stack) {
    }

    /** 饰品被卸下时触发（由 Curios 的 {@code onUnequip} 委托，仅服务端）。 */
    public void onUnequip(LivingEntity entity) {
    }

    /** 穿戴者受到伤害后触发（由 {@code LivingDamageEvent.Post} 分发，仅服务端）。 */
    public void onDamaged(LivingDamageEvent.Post event, LivingEntity entity) {
    }

    /** 穿戴者的护甲即将消耗耐久时触发（由 {@code ArmorHurtEvent} 分发，仅服务端）。 */
    public void onArmorHurt(ArmorHurtEvent event, LivingEntity entity) {
    }
}
