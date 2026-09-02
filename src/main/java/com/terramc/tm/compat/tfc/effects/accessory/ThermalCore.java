package com.terramc.tm.compat.tfc.effects.accessory;

import com.terramc.tm.accessory.AccessoryEffect;
import net.minecraft.core.NonNullList;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * 热核：受到伤害时，若背包内存在足够的木炭/煤炭，则消耗它们，
 * 给予迅捷 I 并立即回复生命值，且有冷却时间。
 * <p>
 * 燃料消耗依赖玩家物品栏，因此仅对玩家生效。
 */
public class ThermalCore extends AccessoryEffect {
    private static final Map<UUID, Long> LAST_TRIGGER = new ConcurrentHashMap<>();

    private final Supplier<Boolean> enabled;
    private final Supplier<Integer> requiredFuel;
    private final Supplier<Double> healAmount;
    private final Supplier<Integer> speedTicks;
    private final Supplier<Integer> cooldownTicks;

    public ThermalCore(Supplier<Boolean> enabled,
                       Supplier<Integer> requiredFuel,
                       Supplier<Double> healAmount,
                       Supplier<Integer> speedTicks,
                       Supplier<Integer> cooldownTicks) {
        this.enabled = enabled;
        this.requiredFuel = requiredFuel;
        this.healAmount = healAmount;
        this.speedTicks = speedTicks;
        this.cooldownTicks = cooldownTicks;
    }

    @Override
    public void onDamaged(LivingDamageEvent.Post event, LivingEntity entity) {
        if (!enabled.get()) {
            return;
        }
        if (isOnCooldown(entity)) {
            return;
        }
        // 燃料消耗依赖玩家物品栏，因此仅对玩家生效
        if (entity instanceof Player player && consumeFuel(player, requiredFuel.get())) {
            LAST_TRIGGER.put(entity.getUUID(), entity.level().getGameTime());
            entity.heal(healAmount.get().floatValue());
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, speedTicks.get(), 0, false, false, true));
        }
    }

    private boolean isOnCooldown(LivingEntity entity) {
        Long last = LAST_TRIGGER.get(entity.getUUID());
        if (last == null) {
            return false;
        }
        return entity.level().getGameTime() - last < cooldownTicks.get();
    }

    private boolean consumeFuel(Player player, int required) {
        NonNullList<ItemStack> items = player.getInventory().items;
        int remaining = required;
        for (ItemStack stack : items) {
            if (remaining <= 0) {
                break;
            }
            if (isFuel(stack)) {
                int take = Math.min(stack.getCount(), remaining);
                stack.shrink(take);
                remaining -= take;
            }
        }
        return remaining <= 0;
    }

    private boolean isFuel(ItemStack stack) {
        return stack.is(Items.CHARCOAL) || stack.is(Items.COAL);
    }
}
