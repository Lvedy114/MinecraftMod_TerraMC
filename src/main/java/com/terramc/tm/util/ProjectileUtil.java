package com.terramc.tm.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Projectile;

import java.util.List;

/**
 * 投掷物相关工具方法。
 */
public final class ProjectileUtil {
    private ProjectileUtil() {
    }

    /**
     * 判断投掷物的实体类型 ID 是否在给定列表内。
     * 用于在不硬编码第三方模组类的情况下，通过配置判定「矛类投掷物」等自定义类别。
     */
    public static boolean matchesType(Projectile projectile, List<? extends String> typeIds) {
        ResourceLocation key = BuiltInRegistries.ENTITY_TYPE.getKey(projectile.getType());
        return key != null && typeIds.contains(key.toString());
    }
}
