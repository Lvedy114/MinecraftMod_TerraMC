package com.terramc.tm.accessory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 饰品注册表：集中管理所有已注册的 {@link AccessoryDefinition}。
 * <p>
 * 使用方式：在模组初始化阶段（例如 {@code TerraMC} 构造器，或各联动包内的 {@code register()} 方法）
 * 调用 {@link #register(AccessoryDefinition)} 即可。
 */
public final class AccessoryRegistry {
    private static final List<AccessoryDefinition> DEFINITIONS = new ArrayList<>();

    private AccessoryRegistry() {
    }

    /** 注册一个饰品定义。 */
    public static void register(AccessoryDefinition definition) {
        DEFINITIONS.add(definition);
    }

    /** 返回所有已注册饰品定义的只读视图。 */
    public static List<AccessoryDefinition> all() {
        return Collections.unmodifiableList(DEFINITIONS);
    }
}
