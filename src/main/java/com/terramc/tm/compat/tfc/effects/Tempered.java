package com.terramc.tm.compat.tfc.effects;

import com.terramc.tm.TerraMC;
import com.terramc.tm.accessory.AccessoryEffect;
import com.terramc.tm.compat.tfc.config.TfcTemperedConfig;
import net.dries007.tfc.common.component.forge.ForgingBonus;
import net.dries007.tfc.common.component.forge.ForgingBonusComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CurioAttributeModifiers;
import top.theillusivec4.curios.common.CuriosRegistry;

/**
 * 千锤百炼：可在群峦砧上锻造，锻造品质决定其提供的护甲与盔甲韧性。
 * <p>
 * 装备时读取自身的群峦锻造品质（{@code tfc:forging_bonus} 数据组件），
 * 并根据品质重写自身的 Curios 属性修饰符（{@code curios:attribute_modifiers} 数据组件），
 * 而非每刻修改玩家临时属性。
 */
public class Tempered extends AccessoryEffect {
    private static final ResourceLocation ARMOR_ID = TerraMC.id("tfc_tempered_armor");
    private static final ResourceLocation TOUGHNESS_ID = TerraMC.id("tfc_tempered_toughness");
    private static final String ACCESSORY_SLOT = "accessory";

    @Override
    public void onEquip(ServerPlayer player, ItemStack stack) {
        if (!TfcTemperedConfig.enabled.get()) {
            return;
        }
        ForgingBonus bonus = ForgingBonusComponent.get(stack);
        int downgrade = downgrade(bonus);

        double armor;
        double toughness;
        if (downgrade < 0) {
            armor = 0;
            toughness = 0;
        } else {
            armor = Math.max(TfcTemperedConfig.minArmor.get(),
                    TfcTemperedConfig.perfectArmor.get() - downgrade * TfcTemperedConfig.armorStep.get());
            toughness = Math.max(TfcTemperedConfig.minToughness.get(),
                    TfcTemperedConfig.perfectToughness.get() - downgrade * TfcTemperedConfig.toughnessStep.get());
        }

        CurioAttributeModifiers.Builder builder = CurioAttributeModifiers.builder();
        if (armor > 0) {
            builder.add(Attributes.ARMOR,
                    new AttributeModifier(ARMOR_ID, armor, AttributeModifier.Operation.ADD_VALUE),
                    ACCESSORY_SLOT);
        }
        if (toughness > 0) {
            builder.add(Attributes.ARMOR_TOUGHNESS,
                    new AttributeModifier(TOUGHNESS_ID, toughness, AttributeModifier.Operation.ADD_VALUE),
                    ACCESSORY_SLOT);
        }
        stack.set(CuriosRegistry.CURIO_ATTRIBUTE_MODIFIERS.get(), builder.build());
    }

    /** 品质降级数：PERFECT=0、EXPERT=1、WELL=2、MODEST=3；NONE（未锻造）返回 -1。 */
    private static int downgrade(ForgingBonus bonus) {
        return switch (bonus) {
            case PERFECT -> 0;
            case EXPERT -> 1;
            case WELL -> 2;
            case MODEST -> 3;
            case NONE -> -1;
        };
    }
}
