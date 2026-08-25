package com.terramc.tm.init;

import com.terramc.tm.TerraMC;
import com.terramc.tm.accessory.AccessoryRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

// 创造模式物品栏注册中心。
public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TerraMC.MODID);

    // 主标签页：自动收录所有已注册的饰品
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = TABS.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.tm.main"))
                    .icon(() -> new ItemStack(Items.DIAMOND))
                    .displayItems((parameters, output) -> {
                        AccessoryRegistry.all().forEach(def -> output.accept(def.item().get()));
                    })
                    .build());

    private ModCreativeTabs() {
    }
}
