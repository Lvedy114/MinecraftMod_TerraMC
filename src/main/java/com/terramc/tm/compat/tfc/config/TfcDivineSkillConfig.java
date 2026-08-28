package com.terramc.tm.compat.tfc.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * 群峦联动饰品「[群峦]神乎其技」的配置。
 * <p>
 * 对应配置文件小节 {@code [accessories.tfc_divine_skill]}，仅在安装了群峦时才会生成。
 */
public final class TfcDivineSkillConfig {
    /** 饰品 ID（与注册名一致）。 */
    public static final String ID = "tfc_divine_skill";

    /** 是否启用该饰品的所有效果。 */
    public static ModConfigSpec.BooleanValue enabled;

    /** 投掷基础伤害加成（点）。 */
    public static ModConfigSpec.DoubleValue thrownDamageBonus;

    /** 投掷后是否高亮。 */
    public static ModConfigSpec.BooleanValue glowEnabled;

    /** 标枪回收范围扩大（格）。 */
    public static ModConfigSpec.DoubleValue recallRange;

    /** 回收路径上对生物造成的伤害（点）。 */
    public static ModConfigSpec.DoubleValue recallPathDamage;

    private TfcDivineSkillConfig() {
    }

    public static void init(ModConfigSpec.Builder builder) {
        builder.push(ID);
        enabled = builder
                .comment("是否启用该饰品效果")
                .define("enabled", true);

        thrownDamageBonus = builder
                .comment("矛类投掷物命中时的基础伤害加成（点）")
                .defineInRange("thrownDamageBonus", 5.0, 0.0, 10000.0);

        glowEnabled = builder
                .comment("矛类投掷物投掷后是否高亮显示")
                .define("glowEnabled", true);

        recallRange = builder
                .comment("标枪回收范围扩大（格）")
                .defineInRange("recallRange", 25.0, 0.0, 64.0);

        recallPathDamage = builder
                .comment("回收过程中对回收路径上的生物造成的伤害（点）")
                .defineInRange("recallPathDamage", 1.0, 0.0, 10000.0);
        builder.pop();
    }
}
