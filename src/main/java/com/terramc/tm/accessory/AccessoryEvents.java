package com.terramc.tm.accessory;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.ArmorHurtEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.event.CurioCanEquipEvent;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.function.Consumer;

/**
 * 饰品效果事件分发器 + 互斥装备检查。
 */
public final class AccessoryEvents {
    private static boolean registered;

    private AccessoryEvents() {
    }

    /**
     * Curios 将装备校验事件发布到 NeoForge 游戏事件总线，因此在模组构造时显式注册监听器。
     */
    public static void register() {
        if (registered) {
            return;
        }
        registered = true;
        // 最后执行装备校验，防止其它监听器随后把结果改回允许。
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, AccessoryEvents::onCurioCanEquip);
        NeoForge.EVENT_BUS.addListener(AccessoryEvents::onLivingHurt);
        NeoForge.EVENT_BUS.addListener(AccessoryEvents::onEntityJoinLevel);
        NeoForge.EVENT_BUS.addListener(AccessoryEvents::onDamaged);
        NeoForge.EVENT_BUS.addListener(AccessoryEvents::onArmorHurt);
    }

    // ===== 互斥装备检查 =====

    /** 阻止装备同组或同款饰品（参考汇流来世 {@code CuriosUtils.noSameCurio}）。 */
    private static void onCurioCanEquip(CurioCanEquipEvent event) {
        ItemStack stack = event.getStack();
        if (!(stack.getItem() instanceof AccessoryItem accessory) || event.getSlotContext().cosmetic()) {
            return;
        }

        CuriosApi.getCuriosInventory(event.getSlotContext().entity()).ifPresent(handler -> {
            for (ICurioStacksHandler sh : handler.getCurios().values()) {
                if (hasConflict(sh.getStacks(), stack, accessory)) {
                    event.setEquipResult(TriState.FALSE);
                    return;
                }
            }
        });
    }

    /** 按汇流来世 CuriosUtils.noSameCurio 的方式遍历真实装备栈，并增加组互斥检查。 */
    private static boolean hasConflict(IDynamicStackHandler stacks, ItemStack candidate, AccessoryItem accessory) {
        for (int i = 0; i < stacks.getSlots(); i++) {
            ItemStack equipped = stacks.getStackInSlot(i);
            if (equipped.isEmpty()) {
                continue;
            }
            if (equipped.getItem() == candidate.getItem()) {
                return true;
            }
            if (accessory.group() != null
                    && equipped.getItem() instanceof AccessoryItem other
                    && accessory.group().equals(other.group())) {
                return true;
            }
        }
        return false;
    }

    // ===== 效果分发 =====

    private static void onLivingHurt(LivingIncomingDamageEvent event) {
        Entity direct = event.getSource().getDirectEntity();
        if (!(direct instanceof Projectile projectile) || !(projectile.getOwner() instanceof ServerPlayer player)) {
            return;
        }
        forEachEquipped(player, effect -> effect.onThrownProjectileHit(event, player, projectile));
    }

    private static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Projectile projectile) || !(projectile.getOwner() instanceof ServerPlayer player)) {
            return;
        }
        forEachEquipped(player, effect -> effect.onProjectileSpawn(event, player, projectile));
    }

    private static void onDamaged(LivingDamageEvent.Post event) {
        if (isServerSide(event.getEntity())) {
            forEachEquipped(event.getEntity(), effect -> effect.onDamaged(event, event.getEntity()));
        }
    }

    private static void onArmorHurt(ArmorHurtEvent event) {
        if (isServerSide(event.getEntity())) {
            forEachEquipped(event.getEntity(), effect -> effect.onArmorHurt(event, event.getEntity()));
        }
    }

    private static void forEachEquipped(LivingEntity entity, Consumer<AccessoryEffect> action) {
        CuriosApi.getCuriosInventory(entity).ifPresent(handler -> {
            for (SlotResult slot : handler.findCurios(AccessoryItem.ACCESSORY_SLOT)) {
                if (slot.stack().getItem() instanceof AccessoryItem accessory) {
                    accessory.effects().forEach(action);
                }
            }
        });
    }

    /** 伤害/护甲类游戏事件双端都会触发，效果只在服务端结算。 */
    private static boolean isServerSide(Entity entity) {
        return entity instanceof LivingEntity living && !living.level().isClientSide;
    }
}
