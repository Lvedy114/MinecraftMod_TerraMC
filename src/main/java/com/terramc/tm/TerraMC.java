package com.terramc.tm;

import com.mojang.logging.LogUtils;
import com.terramc.tm.init.ModBlocks;
import com.terramc.tm.init.ModCreativeTabs;
import com.terramc.tm.init.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

// 注册 ID 为 "tm"，需与 META-INF/neoforge.mods.toml 中的 modId 一致。
@Mod(TerraMC.MODID)
public class TerraMC {
    // 模组注册 ID，所有物品/方块/实体等命名空间统一使用 "tm"
    public static final String MODID = "tm";

    public static final Logger LOGGER = LogUtils.getLogger();

    public TerraMC(IEventBus modEventBus, ModContainer modContainer) {
        // 注册各类 DeferredRegister 到模组事件总线
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModCreativeTabs.TABS.register(modEventBus);

        // 注册配置文件
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
