package com.terramc.tm.accessory;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

/**
 * 饰品物品基类。
 * <p>
 * 每个饰品都是一个物品（{@link Item}），通过 {@link #getAccessoryId()} 关联到
 * {@link AccessoryDefinition}（饰品定义），从而绑定到具体的 {@link AccessoryEffect}（效果）。
 * <p>
 * 本类实现 Curios 的 {@link ICurioItem}，与「汇流来世(Confluence)」使用同一套 Curios API：
 * 饰品只能放入「配饰(accessory)」栏位，装备判定也统一走 Curios（见 {@link AccessoryManager}）。
 */
public class AccessoryItem extends Item implements ICurioItem {
    /** 汇流来世(Confluence)提供的饰品栏位 ID，所有 TerraMC 饰品均放入该栏位。 */
    public static final String ACCESSORY_SLOT = "accessory";

    private final ResourceLocation accessoryId;

    public AccessoryItem(ResourceLocation accessoryId, Item.Properties properties) {
        super(properties);
        this.accessoryId = accessoryId;
    }

    /** 该饰品在饰品注册表中的唯一 ID，例如 {@code tm:tfc_primal_intuition}。 */
    public ResourceLocation getAccessoryId() {
        return accessoryId;
    }

    /** 判断物品堆是否为指定 ID 的饰品。 */
    public static boolean isAccessory(ItemStack stack, ResourceLocation accessoryId) {
        return stack.getItem() instanceof AccessoryItem accessory
                && accessory.getAccessoryId().equals(accessoryId);
    }

    /** 仅允许放入「配饰(accessory)」栏位，其他 Curios 栏位（戒指、项链等）均不可装备。 */
    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return ACCESSORY_SLOT.equals(slotContext.identifier());
    }

    /** 允许右键直接装备到「配饰(accessory)」栏位。 */
    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return canEquip(slotContext, stack);
    }
}
