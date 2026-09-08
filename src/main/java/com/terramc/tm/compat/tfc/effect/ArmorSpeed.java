package com.terramc.tm.compat.tfc.effect;

import com.terramc.tm.TerraMC;
import com.terramc.tm.accessory.AccessoryEffect;
import com.terramc.tm.compat.tfc.TfcHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

import java.util.function.Supplier;

/**
 * 灵动步法：每装备一件群峦护甲，获得一定比例的移动速度加成。
 */
public class ArmorSpeed extends AccessoryEffect {
    private static final ResourceLocation MODIFIER_ID = TerraMC.id("tfc_nimble_footwork_speed");

    private final Supplier<Boolean> enabled;
    private final Supplier<Double> speedPerArmor;

    public ArmorSpeed(Supplier<Boolean> enabled, Supplier<Double> speedPerArmor) {
        this.enabled = enabled;
        this.speedPerArmor = speedPerArmor;
    }

    @Override
    public void onCurioTick(SlotContext slotContext, LivingEntity entity, ItemStack stack) {
        AttributeInstance attr = entity.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
        if (!enabled.get()) {
            attr.removeModifier(MODIFIER_ID);
            return;
        }
        double value = TfcHelper.countTfcArmor(entity) * speedPerArmor.get();
        AttributeModifier existing = attr.getModifier(MODIFIER_ID);
        if (existing != null && existing.amount() == value) {
            return;
        }
        attr.addOrUpdateTransientModifier(new AttributeModifier(MODIFIER_ID, value, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    }

    @Override
    public void onUnequip(LivingEntity entity) {
        entity.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).removeModifier(MODIFIER_ID);
    }
}
