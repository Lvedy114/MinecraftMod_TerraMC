package com.terramc.tm.compat.tfc.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * 群峦联动饰品「[群峦]生存者」的配置。
 * 每装备一件群峦护甲，额外获得护甲值与盔甲韧性。
 */
public final class TfcSurvivorConfig {
    public static final String ID = "tfc_survivor";

    public static ModConfigSpec.BooleanValue enabled;

    /** 每件群峦护甲提供的护甲值。 */
    public static ModConfigSpec.DoubleValue armorPerArmor;

    /** 每件群峦护甲提供的盔甲韧性。 */
    public static ModConfigSpec.DoubleValue toughnessPerArmor;

    private TfcSurvivorConfig() {
    }

    public static void init(ModConfigSpec.Builder builder) {
        builder.push(ID);
        enabled = builder.comment("是否启用该饰品效果").define("enabled", true);
        armorPerArmor = builder
                .comment("每装备一件群峦护甲额外获得的护甲值")
                .defineInRange("armorPerArmor", 0.5, 0.0, 30.0);
        toughnessPerArmor = builder
                .comment("每装备一件群峦护甲额外获得的盔甲韧性")
                .defineInRange("toughnessPerArmor", 0.25, 0.0, 20.0);
        builder.pop();
    }
}
