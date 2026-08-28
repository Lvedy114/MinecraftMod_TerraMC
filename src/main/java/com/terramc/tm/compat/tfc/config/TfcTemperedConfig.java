package com.terramc.tm.compat.tfc.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * 群峦联动饰品「[群峦]千锤百炼」的配置。
 * <p>
 * 该饰品可在群峦砧上锻造（锻造产物仍是自身），锻造品质决定其提供的护甲与盔甲韧性。
 * 品质从高到低：PERFECT、EXPERT、WELL、MODEST，品质每低一级护甲 -armorStep、韧性 -toughnessStep。
 */
public final class TfcTemperedConfig {
    public static final String ID = "tfc_tempered";

    public static ModConfigSpec.BooleanValue enabled;

    /** 完美(PERFECT)品质提供的护甲值。 */
    public static ModConfigSpec.DoubleValue perfectArmor;

    /** 完美(PERFECT)品质提供的盔甲韧性。 */
    public static ModConfigSpec.DoubleValue perfectToughness;

    /** 品质每低一级减少的护甲值。 */
    public static ModConfigSpec.DoubleValue armorStep;

    /** 品质每低一级减少的盔甲韧性。 */
    public static ModConfigSpec.DoubleValue toughnessStep;

    /** 最低提供的护甲值。 */
    public static ModConfigSpec.DoubleValue minArmor;

    /** 最低提供的盔甲韧性。 */
    public static ModConfigSpec.DoubleValue minToughness;

    private TfcTemperedConfig() {
    }

    public static void init(ModConfigSpec.Builder builder) {
        builder.push(ID);
        enabled = builder.comment("是否启用该饰品效果").define("enabled", true);
        perfectArmor = builder
                .comment("完美(PERFECT)品质提供的护甲值")
                .defineInRange("perfectArmor", 10.0, 0.0, 100.0);
        perfectToughness = builder
                .comment("完美(PERFECT)品质提供的盔甲韧性")
                .defineInRange("perfectToughness", 5.0, 0.0, 100.0);
        armorStep = builder
                .comment("品质每低一级减少的护甲值")
                .defineInRange("armorStep", 3.0, 0.0, 100.0);
        toughnessStep = builder
                .comment("品质每低一级减少的盔甲韧性")
                .defineInRange("toughnessStep", 2.0, 0.0, 100.0);
        minArmor = builder
                .comment("最低提供的护甲值")
                .defineInRange("minArmor", 1.0, 0.0, 100.0);
        minToughness = builder
                .comment("最低提供的盔甲韧性")
                .defineInRange("minToughness", 0.5, 0.0, 100.0);
        builder.pop();
    }
}
