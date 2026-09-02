package com.terramc.tm.compat.tfc;

import com.terramc.tm.compat.tfc.weapon.sword.StoneClubItem;
import com.terramc.tm.init.ModItems;
import net.minecraft.world.item.Tier;
import net.neoforged.neoforge.registries.DeferredItem;
import org.confluence.lib.common.component.ModRarity;
import org.confluence.mod.common.component.SwordProjectileComponent;
import org.confluence.mod.common.init.ModTiers;
import org.confluence.mod.common.item.sword.BaseSwordItem;
import org.confluence.mod.common.item.sword.legacy.SwordPrefabs;

import java.util.function.Supplier;

/**
 * 群峦(TFC)专属武器注册清单。
 * <p>
 * 本类只负责列出并注册武器；具体物品行为、弹幕组件和弹幕实体分别放在
 * {@code weapon/sword}、{@code weapon/component} 与 {@code weapon/entity} 中。
 * 武器实现直接复用汇流来世的 Base* 体系。
 */
public final class TfcWeapon {
    /** 使用汇流来世已有星怒弹幕组件的剑类样板。 */
    public static final DeferredItem<BaseSwordItem> STARFALL_SWORD = register(
            "tfc_starfall_sword", ModTiers.COPPER, 2, 3.0F, ModRarity.WHITE,
            SwordPrefabs.PROJ_SWORD.apply(SwordProjectileComponent.STAR_FURY_PROJ)
                    .addTooltip(p -> p.withColor(0x984C11)));

    /** 使用自定义弹幕实体的 TFC 剑类样板。 */
    public static final DeferredItem<StoneClubItem> STONE_CLUB = ModItems.ITEMS.register(
            "tfc_stone_club",
            () -> new StoneClubItem(ModTiers.COPPER, ModRarity.WHITE, 2, 3.0F));

    public static DeferredItem<BaseSwordItem> register(String name, Tier tier, int rawDamage, float rawSpeed,
                                                        BaseSwordItem.ModifierBuilder modifierBuilder) {
        return register(name, tier, rawDamage, rawSpeed, ModRarity.WHITE, modifierBuilder);
    }

    public static DeferredItem<BaseSwordItem> register(String name, Tier tier, int rawDamage, float rawSpeed,
                                                        ModRarity rarity,
                                                        BaseSwordItem.ModifierBuilder modifierBuilder) {
        return ModItems.ITEMS.register(name,
                () -> new BaseSwordItem(tier, rarity, rawDamage, rawSpeed, modifierBuilder));
    }

    private TfcWeapon() {
    }

    /** 供 TerraMC 在 TFC 条件分支中触发本类初始化。 */
    public static void init() {
    }
}
