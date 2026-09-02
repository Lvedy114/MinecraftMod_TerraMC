package com.terramc.tm.compat.tfc.weapon.component;

import com.terramc.tm.compat.tfc.TfcEntities;
import net.minecraft.sounds.SoundEvents;
import org.confluence.mod.common.component.SwordProjectileComponent;
import org.confluence.terraentity.registries.generation.variant.AboveFallenGeneration;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * 石头棒子弹幕的数据组件工厂。
 * <p>
 * 参数沿用汇流来世的 SwordProjectileComponent；生成位置与发射方向由
 * {@code StoneClubItem.genProjectile} 负责，避免把数据配置和物品行为混在注册类中。
 */
public final class StoneClubProjectileComponent {
    private StoneClubProjectileComponent() {
    }

    public static Supplier<SwordProjectileComponent> create() {
        return () -> new SwordProjectileComponent(
                1.0F,
                1.25F,
                1.0F,
                50,
                0.0F,
                10,
                SoundEvents.STONE_HIT.getLocation(),
                TfcEntities.STONE_CLUB_PROJECTILE.getId(),
                Optional.empty(),
                new AboveFallenGeneration(0, 0, 0, 1, 0, 0),
                Optional.empty());
    }
}
