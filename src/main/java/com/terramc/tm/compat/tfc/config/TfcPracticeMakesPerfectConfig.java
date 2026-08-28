package com.terramc.tm.compat.tfc.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * 群峦联动饰品「[群峦]熟能生巧」的配置。
 * <p>
 * 对应配置文件小节 {@code [accessories.tfc_practice_makes_perfect]}，仅在安装了群峦时才会生成。
 */
public final class TfcPracticeMakesPerfectConfig {
    /** 饰品 ID（与注册名一致）。 */
    public static final String ID = "tfc_practice_makes_perfect";

    /** 是否启用该饰品的所有效果。 */
    public static ModConfigSpec.BooleanValue enabled;

    /** 投掷基础伤害加成（点）。 */
    public static ModConfigSpec.DoubleValue thrownDamageBonus;

    /** 投掷后是否高亮。 */
    public static ModConfigSpec.BooleanValue glowEnabled;

    /** 标枪回收范围扩大（格）。 */
    public static ModConfigSpec.DoubleValue recallRange;

    private TfcPracticeMakesPerfectConfig() {
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

        recallRange = builder
                .comment("标枪回收范围扩大（格）")
                .defineInRange("recallRange", 15.0, 0.0, 64.0);
        builder.pop();
    }
}
