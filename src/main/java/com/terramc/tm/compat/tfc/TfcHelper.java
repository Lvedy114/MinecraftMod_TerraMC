package com.terramc.tm.compat.tfc;

import com.terramc.tm.config.JsonConfig;
import com.terramc.tm.util.ProjectileUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * 群峦(TFC)联动通用判定与配置读取工具。
 * <p>
 * 通用数值存于 {@code data/tm/config/tfc/general.json}（key: {@code spear}），
 * 由 {@link JsonConfig} 统一加载，缺失时回退代码默认值。
 */
public final class TfcHelper {
    private static final String GENERAL = "tfc/general";

    private TfcHelper() {
    }

    /** 判断某投掷物是否为群峦「矛类投掷物」（标枪等）。 */
    public static boolean isSpear(Projectile projectile) {
        return ProjectileUtil.matchesType(projectile,
                JsonConfig.getStringList(GENERAL, "spear", "projectileTypes", List.of("tfc:javelin")));
    }

    /** 判断物品堆是否为群峦(TFC)的护甲（以注册名命名空间 tfc 判定）。 */
    public static boolean isTfcArmor(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return key != null && "tfc".equals(key.getNamespace());
    }

    /** 统计实体身上装备的群峦(TFC)护甲件数（0~4）。 */
    public static int countTfcArmor(LivingEntity entity) {
        int count = 0;
        for (ItemStack armor : entity.getArmorSlots()) {
            if (isTfcArmor(armor)) {
                count++;
            }
        }
        return count;
    }
}
