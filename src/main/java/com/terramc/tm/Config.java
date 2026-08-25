package com.terramc.tm;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// 模组通用配置，使用 NeoForge 的 ModConfigSpec API。
@EventBusSubscriber(modid = TerraMC.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue ENABLE_LOG = BUILDER
            .comment("是否在加载时打印调试日志")
            .define("enableLog", true);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean enableLog;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        enableLog = ENABLE_LOG.get();
    }
}
