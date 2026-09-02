package com.terramc.tm.compat.tfc.effects.accessory;

import com.terramc.tm.TerraMC;
import com.terramc.tm.accessory.AccessoryEffect;
import com.terramc.tm.compat.tfc.config.TfcConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

import java.util.function.Supplier;

/**
 * 生存者：每装备一件群峦护甲，额外获得护甲值与盔甲韧性。
 */
public class ArmorDefense extends AccessoryEffect {
    private static final ResourceLocation ARMOR_ID = TerraMC.id("tfc_survivor_armor");
    private static final ResourceLocation TOUGHNESS_ID = TerraMC.id("tfc_survivor_toughness");

    private final Supplier<Boolean> enabled;
    private final Supplier<Double> armorPerArmor;
    private final Supplier<Double> toughnessPerArmor;

    public ArmorDefense(Supplier<Boolean> enabled,
                        Supplier<Double> armorPerArmor,
                        Supplier<Double> toughnessPerArmor) {
        this.enabled = enabled;
        this.armorPerArmor = armorPerArmor;
        this.toughnessPerArmor = toughnessPerArmor;
    }

    @Override
    public void onCurioTick(SlotContext slotContext, LivingEntity entity, ItemStack stack) {
        AttributeInstance armorAttr = entity.getAttributes().getInstance(Attributes.ARMOR);
        AttributeInstance toughnessAttr = entity.getAttributes().getInstance(Attributes.ARMOR_TOUGHNESS);
        if (!enabled.get()) {
            armorAttr.removeModifier(ARMOR_ID);
            toughnessAttr.removeModifier(TOUGHNESS_ID);
            return;
        }
        int count = TfcConfig.countTfcArmor(entity);
        update(armorAttr, ARMOR_ID, count * armorPerArmor.get(), AttributeModifier.Operation.ADD_VALUE);
        update(toughnessAttr, TOUGHNESS_ID, count * toughnessPerArmor.get(), AttributeModifier.Operation.ADD_VALUE);
    }

    @Override
    public void onUnequip(LivingEntity entity) {
        entity.getAttributes().getInstance(Attributes.ARMOR).removeModifier(ARMOR_ID);
        entity.getAttributes().getInstance(Attributes.ARMOR_TOUGHNESS).removeModifier(TOUGHNESS_ID);
    }

    private static void update(AttributeInstance attr, ResourceLocation id, double value, AttributeModifier.Operation op) {
        AttributeModifier existing = attr.getModifier(id);
        if (existing != null && existing.amount() == value) {
            return;
        }
        attr.addOrUpdateTransientModifier(new AttributeModifier(id, value, op));
    }
}
