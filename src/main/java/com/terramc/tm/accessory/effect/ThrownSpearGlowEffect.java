package com.terramc.tm.accessory.effect;

import com.terramc.tm.accessory.AccessoryEffect;
import com.terramc.tm.config.TfcPrimalIntuitionConfig;
import com.terramc.tm.util.ProjectileUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

/**
 * 投掷高亮效果：矛类投掷物在投出（加入世界）后获得高亮显示。
 */
public class ThrownSpearGlowEffect extends AccessoryEffect {
    public ThrownSpearGlowEffect(String id) {
        super(id);
    }

    @Override
    public void onProjectileSpawn(EntityJoinLevelEvent event, ServerPlayer owner, Projectile projectile) {
        if (!TfcPrimalIntuitionConfig.enabled.get()) {
            return;
        }
        if (!TfcPrimalIntuitionConfig.glowEnabled.get()) {
            return;
        }
        if (!ProjectileUtil.matchesType(projectile, TfcPrimalIntuitionConfig.spearProjectileTypes.get())) {
            return;
        }
        ((Entity) projectile).setGlowingTag(true);
    }
}
