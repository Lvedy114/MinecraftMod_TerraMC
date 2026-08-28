package com.terramc.tm.compat.tfc.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * 群峦联动饰品「[群峦]热核」的配置。
 * 受到伤害时消耗燃料，获得迅捷与治疗。
 */
public final class TfcThermalCoreConfig {
    public static final String ID = "tfc_thermal_core";

    public static ModConfigSpec.BooleanValue enabled;

    /** 触发所需的木炭/煤炭数量。 */
    public static ModConfigSpec.IntValue requiredFuel;

    /** 立即回复的生命值。 */
    public static ModConfigSpec.DoubleValue healAmount;

    /** 迅捷效果的持续时间（刻）。 */
    public static ModConfigSpec.IntValue speedTicks;

    /** 冷却时间（刻）。 */
    public static ModConfigSpec.IntValue cooldownTicks;

    private TfcThermalCoreConfig() {
    }

    public static void init(ModConfigSpec.Builder builder) {
        builder.push(ID);
        enabled = builder.comment("是否启用该饰品效果").define("enabled", true);
        requiredFuel = builder
                .comment("触发所需的木炭/煤炭数量")
                .defineInRange("requiredFuel", 5, 1, 64);
        healAmount = builder
                .comment("触发时立即回复的生命值")
                .defineInRange("healAmount", 4.0, 0.0, 1000.0);
        speedTicks = builder
                .comment("触发时给予迅捷效果的持续时间（刻，20 刻 = 1 秒）")
                .defineInRange("speedTicks", 100, 0, 72000);
        cooldownTicks = builder
                .comment("触发后的冷却时间（刻，20 刻 = 1 秒）")
                .defineInRange("cooldownTicks", 200, 0, 72000);
        builder.pop();
    }
}
