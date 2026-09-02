package com.terramc.tm.compat.tfc.effects.accessory;

import com.terramc.tm.accessory.AccessoryEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.ArmorHurtEvent;

import java.util.function.Supplier;

/**
 * 早有准备：身上的护甲不再消耗耐久。
 */
public class NoArmorDurability extends AccessoryEffect {
    private final Supplier<Boolean> enabled;

    public NoArmorDurability(Supplier<Boolean> enabled) {
        this.enabled = enabled;
    }

    @Override
    public void onArmorHurt(ArmorHurtEvent event, LivingEntity entity) {
        if (!enabled.get()) {
            return;
        }
        for (EquipmentSlot slot : event.getArmorMap().keySet()) {
            event.setNewDamage(slot, 0.0F);
        }
    }
}
