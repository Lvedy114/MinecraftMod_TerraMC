package com.terramc.tm.util;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.function.Supplier;

public class ModAttributeHelper {

    /**
     * 创建一个简单的属性修饰符 Supplier。
     *
     * @param attribute 要修改的属性 (例如 Attributes.ATTACK_DAMAGE)
     * @param amount    修改的数值 (例如 4.0)
     * @param operation 修改方式 (例如 AttributeModifier.Operation.ADD_VALUE)
     * @param id        修饰符的唯一 ID (ResourceLocation)
     * @param name      修饰符的名称 (仅用于标识)
     * @return 一个 Supplier，用于生成包含该修饰符的 Multimap
     */
    public static Supplier<Multimap<Holder<Attribute>, AttributeModifier>> simpleModifier(
            Holder<Attribute> attribute,
            double amount,
            AttributeModifier.Operation operation,
            ResourceLocation id,
            String name) {
        return () -> {
            Multimap<Holder<Attribute>, AttributeModifier> modifiers = ArrayListMultimap.create();
            modifiers.put(attribute, new AttributeModifier(id, amount, operation));
            return modifiers;
        };
    }
}
