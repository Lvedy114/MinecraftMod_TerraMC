package com.terramc.tm.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * TerraMC 全局 TOML 配置（生成于 {@code config/tm-common.toml}）。
 * <p>
 * 仅存放全局通用开关；各联动的饰品<b>数值</b>已改为 JSON 数据驱动
 * （{@code data/tm/config/<联动模组id>/<分类>.json}，见 {@link JsonConfig}），
 * 不再需要为每个饰品编写 ModConfigSpec 配置类。
 * <pre>
 * [general]
 *     # 全局通用配置
 *     debugLog = false
 * </pre>
 */
public final class TerraConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // ===== general 通用配置 =====
    public static final ModConfigSpec.BooleanValue DEBUG_LOG;

    static {
        BUILDER.push("general");
        DEBUG_LOG = BUILDER
                .comment("是否输出调试日志")
                .define("debugLog", false);
        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    private TerraConfig() {
    }
}
