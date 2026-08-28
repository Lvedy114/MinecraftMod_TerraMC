package com.terramc.tm.compat.tfc.effects;

import com.terramc.tm.accessory.AccessoryEffect;
import com.terramc.tm.compat.tfc.config.TfcConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.entity.living.ArmorHurtEvent;

/**
 * 早有准备：身上的群峦护甲不再消耗耐久。
 */
public class NoArmorDurability extends AccessoryEffect {
    private final ModConfigSpec.BooleanValue enabled;

    public NoArmorDurability(ModConfigSpec.BooleanValue enabled) {
        this.enabled = enabled;
    }

    @Override
    public void onArmorHurt(ArmorHurtEvent event, ServerPlayer player) {
        if (!enabled.get()) {
            return;
        }
        for (EquipmentSlot slot : event.getArmorMap().keySet()) {
            ItemStack armor = event.getArmorItemStack(slot);
            if (TfcConfig.isTfcArmor(armor)) {
                event.setNewDamage(slot, 0.0F);
            }
        }
    }
}
