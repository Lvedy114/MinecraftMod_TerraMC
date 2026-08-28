package com.terramc.tm.compat.tfc.effects.javelin;

import com.terramc.tm.accessory.AccessoryEffect;
import com.terramc.tm.compat.tfc.config.TfcConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

/**
 * 投掷基础伤害加成效果：群峦矛类投掷物命中时，伤害额外增加（数值来自配置）。
 */
public class ThrownSpearDamageBonusEffect extends AccessoryEffect {
    private final ModConfigSpec.BooleanValue enabled;
    private final ModConfigSpec.DoubleValue thrownDamageBonus;

    public ThrownSpearDamageBonusEffect(ModConfigSpec.BooleanValue enabled, ModConfigSpec.DoubleValue thrownDamageBonus) {
        this.enabled = enabled;
        this.thrownDamageBonus = thrownDamageBonus;
    }

    @Override
    public void onThrownProjectileHit(LivingIncomingDamageEvent event, ServerPlayer owner, Projectile projectile) {
        if (!enabled.get()) {
            return;
        }
        if (!TfcConfig.isSpear(projectile)) {
            return;
        }
        event.setAmount(event.getAmount() + thrownDamageBonus.get().floatValue());
    }
}
