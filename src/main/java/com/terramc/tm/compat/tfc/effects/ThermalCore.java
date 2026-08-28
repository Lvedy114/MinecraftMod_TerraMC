package com.terramc.tm.compat.tfc.effects;

import com.terramc.tm.accessory.AccessoryEffect;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 热核：受到伤害时，若背包内存在足够的木炭/煤炭，则消耗它们，
 * 给予迅捷 I 并立即回复生命值，且有冷却时间。
 */
public class ThermalCore extends AccessoryEffect {
    private static final Map<UUID, Long> LAST_TRIGGER = new ConcurrentHashMap<>();

    private final ModConfigSpec.BooleanValue enabled;
    private final ModConfigSpec.IntValue requiredFuel;
    private final ModConfigSpec.DoubleValue healAmount;
    private final ModConfigSpec.IntValue speedTicks;
    private final ModConfigSpec.IntValue cooldownTicks;

    public ThermalCore(ModConfigSpec.BooleanValue enabled,
                       ModConfigSpec.IntValue requiredFuel,
                       ModConfigSpec.DoubleValue healAmount,
                       ModConfigSpec.IntValue speedTicks,
                       ModConfigSpec.IntValue cooldownTicks) {
        this.enabled = enabled;
        this.requiredFuel = requiredFuel;
        this.healAmount = healAmount;
        this.speedTicks = speedTicks;
        this.cooldownTicks = cooldownTicks;
    }

    @Override
    public void onDamaged(LivingDamageEvent.Post event, ServerPlayer player) {
        if (!enabled.get()) {
            return;
        }
        if (isOnCooldown(player)) {
            return;
        }
        if (!consumeFuel(player, requiredFuel.get())) {
            return;
        }
        LAST_TRIGGER.put(player.getUUID(), player.level().getGameTime());
        player.heal(healAmount.get().floatValue());
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, speedTicks.get(), 0, false, false, true));
    }

    private boolean isOnCooldown(ServerPlayer player) {
        Long last = LAST_TRIGGER.get(player.getUUID());
        if (last == null) {
            return false;
        }
        return player.level().getGameTime() - last < cooldownTicks.get();
    }

    private boolean consumeFuel(ServerPlayer player, int required) {
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
