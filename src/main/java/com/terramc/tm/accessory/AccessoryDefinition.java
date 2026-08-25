package com.terramc.tm.accessory;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.Supplier;

/**
 * 饰品定义：把「饰品 ID + 物品 + 效果列表」绑定在一起，构成一个完整的饰品。
 * <p>
 * 通过 {@link AccessoryRegistry#register(AccessoryDefinition)} 注册后，即可被
 * {@link AccessoryEvents} 和 {@link AccessoryManager} 识别与分发。
 */
public final class AccessoryDefinition {
    private final ResourceLocation id;
    private final Supplier<? extends Item> item;
    private final List<AccessoryEffect> effects;

    public AccessoryDefinition(ResourceLocation id, Supplier<? extends Item> item, AccessoryEffect... effects) {
        this.id = id;
        this.item = item;
        this.effects = List.of(effects);
    }

    /** 饰品唯一 ID，例如 {@code tm:tfc_primal_intuition}。 */
    public ResourceLocation id() {
        return id;
    }

    /** 饰品对应的物品。 */
    public Supplier<? extends Item> item() {
        return item;
    }

    /** 该饰品挂载的所有效果（只读）。 */
    public List<AccessoryEffect> effects() {
        return effects;
    }
}
