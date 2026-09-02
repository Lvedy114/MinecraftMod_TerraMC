package com.terramc.tm.compat.tfc.effects.accessory.javelin;

import com.terramc.tm.accessory.AccessoryEffect;
import com.terramc.tm.compat.tfc.config.TfcConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import java.util.function.Supplier;

/**
 * 投掷高亮效果：群峦矛类投掷物在投出（加入世界）后获得高亮显示。
 */
public class ThrownSpearGlowEffect extends AccessoryEffect {
    private final Supplier<Boolean> enabled;
    private final Supplier<Boolean> glowEnabled;

    public ThrownSpearGlowEffect(Supplier<Boolean> enabled, Supplier<Boolean> glowEnabled) {
        this.enabled = enabled;
        this.glowEnabled = glowEnabled;
    }

    @Override
    public void onProjectileSpawn(EntityJoinLevelEvent event, ServerPlayer owner, Projectile projectile) {
        if (!enabled.get() || !glowEnabled.get()) {
            return;
        }
        if (!TfcConfig.isSpear(projectile)) {
            return;
        }
        ((Entity) projectile).setGlowingTag(true);
    }
}
