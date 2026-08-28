package com.terramc.tm.compat.tfc.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * 群峦联动饰品「[群峦]原始力量」的配置。
 * 攻击伤害与攻击速度的固定加成。
 */
public final class TfcPrimalStrengthConfig {
    public static final String ID = "tfc_primal_strength";

    public static ModConfigSpec.BooleanValue enabled;

    /** 额外攻击伤害（点）。 */
    public static ModConfigSpec.DoubleValue attackDamage;

    /** 攻击速度加成（比例，负数为降低）。 */
    public static ModConfigSpec.DoubleValue attackSpeedMultiplier;

    private TfcPrimalStrengthConfig() {
    }

    public static void init(ModConfigSpec.Builder builder) {
        builder.push(ID);
        enabled = builder.comment("是否启用该饰品效果").define("enabled", true);
        attackDamage = builder
                .comment("额外攻击伤害（点）")
                .defineInRange("attackDamage", 4.0, -2048.0, 2048.0);
        attackSpeedMultiplier = builder
                .comment("攻击速度加成（比例，负数为降低）")
                .defineInRange("attackSpeedMultiplier", -0.3, -1.0, 10.0);
        builder.pop();
    }
}
