package com.terramc.tm.accessory.effect;

import com.terramc.tm.accessory.AccessoryEffect;
import com.terramc.tm.config.TfcPrimalIntuitionConfig;
import com.terramc.tm.util.ProjectileUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

/**
 * 投掷基础伤害加成效果：矛类投掷物命中时，伤害额外增加（数值来自配置）。
 */
public class ThrownSpearDamageBonusEffect extends AccessoryEffect {
    public ThrownSpearDamageBonusEffect(String id) {
        super(id);
    }

    @Override
    public void onThrownProjectileHit(LivingIncomingDamageEvent event, ServerPlayer owner, Projectile projectile) {
        if (!TfcPrimalIntuitionConfig.enabled.get()) {
            return;
        }
        if (!ProjectileUtil.matchesType(projectile, TfcPrimalIntuitionConfig.spearProjectileTypes.get())) {
            return;
        }
        float bonus = TfcPrimalIntuitionConfig.thrownDamageBonus.get().floatValue();
        event.setAmount(event.getAmount() + bonus);
    }
}
