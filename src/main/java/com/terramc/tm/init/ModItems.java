package com.terramc.tm.init;

import com.terramc.tm.TerraMC;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.minecraft.world.item.Item;

// 物品注册中心。饰品(accessory)、装备(equipment)等道具在此注册。
public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TerraMC.MODID);

    // 示例：注册一个普通物品
    // public static final DeferredItem<Item> EXAMPLE = ITEMS.registerSimpleItem("example", new Item.Properties());

    private ModItems() {
    }
}
