package com.terramc.tm.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.terramc.tm.TerraMC.MODID;

// 创造模式物品栏注册中心。
public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // 主标签页：自动收录所有已注册的饰品物品与武器
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = TABS.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.tm.main"))
                    .icon(() -> new ItemStack(Items.DIAMOND))
                    .displayItems((parameters, output) -> {
                        ModItems.ITEMS.getEntries().stream()
                                .map(DeferredHolder::get)
                                .filter(item -> {
                                    ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
                                    return key != null && MODID.equals(key.getNamespace());
                                })
                                .forEach(item -> output.accept(item));
                    })
                    .build());

    private ModCreativeTabs() {
    }
}
