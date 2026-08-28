package com.terramc.tm.compat.tfc.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * 群峦联动饰品「[群峦]灵动步法」的配置。
 * 每装备一件群峦护甲，获得一定比例的移动速度加成。
 */
public final class TfcNimbleFootworkConfig {
    public static final String ID = "tfc_nimble_footwork";

    public static ModConfigSpec.BooleanValue enabled;

    /** 每件群峦护甲提供的移动速度加成（比例）。 */
    public static ModConfigSpec.DoubleValue speedPerArmor;

    private TfcNimbleFootworkConfig() {
    }

    public static void init(ModConfigSpec.Builder builder) {
        builder.push(ID);
        enabled = builder.comment("是否启用该饰品效果").define("enabled", true);
        speedPerArmor = builder
                .comment("每装备一件群峦护甲获得的移动速度加成（比例）")
                .defineInRange("speedPerArmor", 0.015, 0.0, 10.0);
        builder.pop();
    }
}
