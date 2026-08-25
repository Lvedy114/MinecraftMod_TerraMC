package com.terramc.tm.init;

import com.terramc.tm.TerraMC;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredRegister;

// 创造模式物品栏注册中心。
public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TerraMC.MODID);

    // 示例：注册一个创造标签页
    // public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = TABS.register("main",
    //         () -> CreativeModeTab.builder()
    //                 .title(Component.translatable("itemGroup.tm.main"))
    //                 .icon(() -> new ItemStack(Items.DIAMOND))
    //                 .build());

    private ModCreativeTabs() {
    }
}
