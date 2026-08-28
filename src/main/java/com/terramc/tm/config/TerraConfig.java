package com.terramc.tm.config;

import com.terramc.tm.compat.Integrations;
import com.terramc.tm.compat.tfc.config.*;
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
 * [tfc]
 *     # 群峦联动通用配置（仅在安装群峦时生成）
 *
 * [accessories.&lt;饰品id&gt;]
 *     # 每个饰品一个独立小节，字段由对应联动包的配置类定义（仅在对应联动模组已安装时生成）
 * </pre>
 * <p>
 * <b>扩展方式</b>：为每个联动的新饰品新建配置类（仿照 {@code compat/tfc/TfcPrimalIntuitionConfig}），
 * 提供静态 {@code init(ModConfigSpec.Builder)} 方法，并在本类静态块中以
 * {@code Integrations.isLoaded(...)} 保护后调用，即可自动获得独立配置小节。
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

        // ===== 群峦联动配置 =====
        if (Integrations.isTfc()) {
            TfcConfig.init(BUILDER);

            BUILDER.push("accessories");
            TfcPrimalIntuitionConfig.init(BUILDER);
            TfcPracticeMakesPerfectConfig.init(BUILDER);
            TfcDivineSkillConfig.init(BUILDER);
            TfcNimbleFootworkConfig.init(BUILDER);
            TfcSurvivorConfig.init(BUILDER);
            TfcEarlyPreparationConfig.init(BUILDER);
            TfcPrimalStrengthConfig.init(BUILDER);
            TfcThermalCoreConfig.init(BUILDER);
            TfcThermalCoreProConfig.init(BUILDER);
            TfcTemperedConfig.init(BUILDER);
            BUILDER.pop();
        }
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    private TerraConfig() {
    }

    /** 校验字符串是否为合法的 ResourceLocation，用于字符串列表校验器。 */
    public static boolean isResourceLocation(Object obj) {
        return obj instanceof String str && ResourceLocation.tryParse(str) != null;
    }
}
