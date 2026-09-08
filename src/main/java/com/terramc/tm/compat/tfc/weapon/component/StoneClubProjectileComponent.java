package com.terramc.tm.compat.tfc.weapon.component;

import com.terramc.tm.compat.tfc.TfcEntities;
import net.minecraft.sounds.SoundEvents;
import org.confluence.mod.common.component.SwordProjectileComponent;
import org.confluence.terraentity.registries.generation.variant.ForwardGeneration;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * 石头棒子弹幕的数据组件工厂。
 * <p>
 * 参数沿用汇流来世的 {@code SwordProjectileComponent}（顺序见该 record 的 javadoc），
 * 并由 {@code BaseSwordItem#genProjectile} 基类管线统一消费：
 * <ul>
 *   <li>damageFactor：剑气伤害系数，实际伤害 = 系数 × 攻击者攻击力属性</li>
 *   <li>baseSpeed：剑气基础速度，实际速度 = baseSpeed × 远程速度属性</li>
 *   <li>cooldown：剑气冷却（tick），由 SwordProjectilePacketC2S 消费</li>
 * </ul>
 * 生成器使用 ForwardGeneration（沿视线直线发射），与汇流冰雪刀/魔剑弹幕一致。
 */
public final class StoneClubProjectileComponent {

    private StoneClubProjectileComponent() {
    }

    public static Supplier<SwordProjectileComponent> create() {
        return () -> new SwordProjectileComponent(
                1.2F,   // damageFactor：tooltip“剑气-伤害”
                0.8F,  // baseSpeed：tooltip“剑气-速度”
                1.0F,   // acceleration：飞行速度保持
                80,     // existTicks：寿命（tick），基类 tick 统一判定
                0.0F,   // gravity
                15,     // cooldown：tooltip“剑气-冷却”
                SoundEvents.STONE_HIT.getLocation(),
                TfcEntities.STONE_CLUB_PROJECTILE.getId(),
                Optional.empty(),
                ForwardGeneration.of(0, 0),
                Optional.empty());
    }
}
