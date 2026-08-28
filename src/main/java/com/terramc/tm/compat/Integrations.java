package com.terramc.tm.compat;

import net.neoforged.fml.ModList;

/**
 * 联动模组检测工具。
 * <p>
 * TerraMC 仅<b>强制依赖</b>「汇流来世(Confluence)」，其余联动模组均为<b>可选</b>：
 * 只有当玩家同时安装了 TerraMC 与该联动模组时，其专属内容才会被注册与展示。
 * <p>
 * 新增联动时：在此登记该模组的 mod id 常量，并在 {@code compat/&lt;modid&gt;} 包内提供注册入口，
 * 然后在 {@link #isLoaded(String)} 的保护下于 {@code TerraMC} / {@code TerraConfig} 中按需调用。
 */
public final class Integrations {
    /** 群峦 (TerraFirmaCraft)，需要 Minecraft 1.21.1 的 4.2.0 及以上版本。 */
    public static final String TFC = "tfc";

    private Integrations() {
    }

    /** 判断某个模组是否已安装。 */
    public static boolean isLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    /** 群峦(TFC)是否已安装。 */
    public static boolean isTfc() {
        return isLoaded(TFC);
    }
}
