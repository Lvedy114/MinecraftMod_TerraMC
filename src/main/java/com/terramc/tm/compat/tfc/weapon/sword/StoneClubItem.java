package com.terramc.tm.compat.tfc.weapon.sword;

import com.terramc.tm.compat.tfc.weapon.component.StoneClubProjectileComponent;
import net.minecraft.world.item.Tier;
import org.confluence.lib.common.component.ModRarity;
import org.confluence.mod.common.item.sword.BaseSwordItem;
import org.confluence.mod.common.item.sword.legacy.SwordPrefabs;

/**
 * 石头做的棒子：继承汇流来世 BaseSwordItem，挥动时向前发射一枚不断翻滚的石头弹幕。
 * <p>
 * 剑气三属性（伤害/速度/冷却）全部由 {@code BaseSwordItem#genProjectile} 基类管线消费：
 * 伤害 = damageFactor × 攻击者攻击力属性，速度 = baseSpeed × 远程速度属性，冷却由
 * {@code SwordProjectilePacketC2S} 统一添加，因此不覆写 genProjectile。
 */
public final class StoneClubItem extends BaseSwordItem {

    public StoneClubItem(Tier tier, ModRarity rarity, int damage, float speed) {
        super(tier, rarity, damage, speed,
                SwordPrefabs.PROJ_SWORD.apply(StoneClubProjectileComponent.create())
                        .addTooltip(p -> p.withColor(0x777777)));
    }
}
