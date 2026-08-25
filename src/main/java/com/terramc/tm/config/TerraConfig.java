package com.terramc.tm.config;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * TerraMC 全局配置。
 * <p>
 * 配置文件结构（生成于 {@code config/tm-common.toml}）：
 * <pre>
 * [general]
 *     # 全局通用配置
 *     debugLog = false
 *
 * [accessories.&lt;饰品id&gt;]
 *     # 每个饰品一个独立小节，字段由该饰品的配置类（如 {@link TfcPrimalIntuitionConfig}）定义
 * </pre>
 * <p>
 * <b>扩展方式</b>：为每个新饰品新建一个配置类（仿照 {@link TfcPrimalIntuitionConfig}），
 * 提供静态 {@code init(ModConfigSpec.Builder)} 方法并在本类静态块中调用，即可自动获得独立配置小节。
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

        // ===== accessories 饰品配置 =====
        BUILDER.push("accessories");
        TfcPrimalIntuitionConfig.init(BUILDER);
        // 新增饰品：OtherAccessoryConfig.init(BUILDER);
        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    private TerraConfig() {
    }

    /** 校验字符串是否为合法的 ResourceLocation，用于字符串列表校验器。 */
    static boolean isResourceLocation(Object obj) {
        return obj instanceof String str && ResourceLocation.tryParse(str) != null;
    }
}
