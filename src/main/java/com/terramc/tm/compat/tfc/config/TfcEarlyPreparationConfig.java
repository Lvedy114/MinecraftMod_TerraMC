package com.terramc.tm.compat.tfc.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * 群峦联动饰品「[群峦]早有准备」的配置。
 * 身上的群峦护甲不再消耗耐久。
 */
public final class TfcEarlyPreparationConfig {
    public static final String ID = "tfc_early_preparation";

    public static ModConfigSpec.BooleanValue enabled;

    private TfcEarlyPreparationConfig() {
    }

    public static void init(ModConfigSpec.Builder builder) {
        builder.push(ID);
        enabled = builder.comment("是否启用该饰品效果").define("enabled", true);
        builder.pop();
    }
}
