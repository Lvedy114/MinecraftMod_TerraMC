package com.terramc.tm;

import com.mojang.logging.LogUtils;
import com.terramc.tm.accessory.AccessoryEvents;
import com.terramc.tm.compat.Integrations;
import com.terramc.tm.compat.tfc.TfcAccessories;
import com.terramc.tm.compat.tfc.TfcEntities;
import com.terramc.tm.compat.tfc.TfcWeapon;
import com.terramc.tm.config.JsonConfig;
import com.terramc.tm.config.TerraConfig;
import com.terramc.tm.init.ModBlocks;
import com.terramc.tm.init.ModCreativeTabs;
import com.terramc.tm.init.ModItems;
import net.minecraft.resources.ResourceLocation;
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
        // 联动内容：仅当对应联动模组已安装时才注册（见 Integrations）
        if (Integrations.isTfc()) {
            TfcAccessories.init();
            TfcWeapon.init();
            TfcEntities.init();
        }

        // Curios 装备校验和饰品效果均发布在 NeoForge 游戏事件总线上
        AccessoryEvents.register();

        // JSON 数值配置随数据包重载加载（data/tm/config/**）
        JsonConfig.register();

        // 注册各类 DeferredRegister 到模组事件总线
        ModItems.ITEMS.register(modEventBus);
        if (Integrations.isTfc()) {
            TfcEntities.ENTITIES.register(modEventBus);
        }
        ModBlocks.BLOCKS.register(modEventBus);
        ModCreativeTabs.TABS.register(modEventBus);

        // 注册配置文件
        modContainer.registerConfig(ModConfig.Type.COMMON, TerraConfig.SPEC);
    }

    // 便捷方法：生成命名空间为 "tm" 的 ResourceLocation
    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
