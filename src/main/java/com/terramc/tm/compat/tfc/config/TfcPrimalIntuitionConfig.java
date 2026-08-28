package com.terramc.tm.compat.tfc.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * 群峦联动饰品「[群峦]原始直觉」的配置。
 * <p>
 * 对应配置文件小节 {@code [accessories.tfc_primal_intuition]}，仅在安装了群峦时才会生成。
 */
public final class TfcPrimalIntuitionConfig {
    /** 饰品 ID（与注册名一致）。 */
    public static final String ID = "tfc_primal_intuition";

    /** 是否启用该饰品的所有效果。 */
    public static ModConfigSpec.BooleanValue enabled;

    /** 投掷基础伤害加成（点）。 */
    public static ModConfigSpec.DoubleValue thrownDamageBonus;

    /** 投掷后是否高亮。 */
    public static ModConfigSpec.BooleanValue glowEnabled;

    private TfcPrimalIntuitionConfig() {
    }

    public static void init(ModConfigSpec.Builder builder) {
        builder.push(ID);
        enabled = builder
                .comment("是否启用该饰品效果")
                .define("enabled", true);

        thrownDamageBonus = builder
                .comment("矛类投掷物命中时的基础伤害加成（点）")
                .defineInRange("thrownDamageBonus", 4.0, 0.0, 10000.0);

        glowEnabled = builder
                .comment("矛类投掷物投掷后是否高亮显示")
                .define("glowEnabled", true);
        builder.pop();
    }
}
