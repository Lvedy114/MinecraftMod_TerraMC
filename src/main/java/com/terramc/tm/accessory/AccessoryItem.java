package com.terramc.tm.accessory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.function.Supplier;

/**
 * 饰品物品基类。
 * <p>
 * 实现 Curios 的 {@link ICurioItem}。「组内互斥」和「不可重复装备」通过
 * {@link AccessoryEvents} 监听 {@code CurioCanEquipEvent} 实现，而非覆写 {@code canEquip}。
 * <p>
 * 支持「组(group)」：同一组的饰品互斥，只能装备其中一件。
 */
public class AccessoryItem extends Item implements ICurioItem {
    /** 「配饰」栏位 ID */
    public static final String ACCESSORY_SLOT = "accessory";

    private final List<AccessoryEffect> effects;
    private final ResourceLocation group;
    private final Supplier<Multimap<Holder<Attribute>, AttributeModifier>> modifiersSupplier;

    public AccessoryItem(Item.Properties properties, AccessoryEffect... effects) {
        this(properties, null, effects);
    }

    public AccessoryItem(Item.Properties properties, ResourceLocation group, AccessoryEffect... effects) {
        this(properties, group, ArrayListMultimap::create, effects);
    }

    public AccessoryItem(Item.Properties properties, ResourceLocation group,
                         Supplier<Multimap<Holder<Attribute>, AttributeModifier>> modifiersSupplier,
                         AccessoryEffect... effects) {
        super(properties);
        this.group = group;
        this.effects = List.of(effects);
        this.modifiersSupplier = modifiersSupplier;
    }

    public List<AccessoryEffect> effects() {
        return effects;
    }

    public ResourceLocation group() {
        return group;
    }

    /**
     * 允许 Curios 的右键快捷装备继续执行；真正的同款/同组校验由
     * {@code CurioCanEquipEvent} 在写入槽位前统一裁决。
     */
    @Override
    public boolean canEquipFromUse(SlotContext ctx, ItemStack stack) {
        return true;
    }

    @Override
    public void onEquip(SlotContext ctx, ItemStack prevStack, ItemStack newStack) {
        if (isServerSideWearer(ctx)) {
            effects.forEach(e -> e.onEquip(ctx.entity(), newStack));
        }
    }

    @Override
    public void onUnequip(SlotContext ctx, ItemStack newStack, ItemStack prevStack) {
        if (isServerSideWearer(ctx)) {
            effects.forEach(e -> e.onUnequip(ctx.entity()));
        }
    }

    /**
     * Curios 对每个已装备的饰品双端调用 {@code curioTick}；这里收口为
     * 「仅服务端、仅真实配饰槽位」再分发给效果（美容槽位与其他槽位类型不触发效果）。
     */
    @Override
    public void curioTick(SlotContext ctx, ItemStack stack) {
        if (ctx.cosmetic() || !ACCESSORY_SLOT.equals(ctx.identifier())) {
            return;
        }
        if (isServerSideWearer(ctx)) {
            effects.forEach(e -> e.onCurioTick(ctx, ctx.entity(), stack));
        }
    }

    /** Curios 支持任意生物佩戴饰品；效果只在服务端触发。 */
    private static boolean isServerSideWearer(SlotContext ctx) {
        return ctx.entity() instanceof LivingEntity entity && !entity.level().isClientSide;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(Component.translatable("tooltip." + stack.getDescriptionId() + ".0").withStyle(ChatFormatting.WHITE));
        if (group != null) {
            tooltip.add(Component.translatable("tooltip.tm.group_exclusive").withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        return modifiersSupplier.get();
    }
}
