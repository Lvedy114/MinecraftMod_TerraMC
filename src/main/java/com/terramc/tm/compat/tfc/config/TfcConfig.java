package com.terramc.tm.compat.tfc.config;

import com.terramc.tm.config.TerraConfig;
import com.terramc.tm.util.ProjectileUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

/**
 * 群峦(TFC)联动通用配置。
 * <p>
 * 对应配置文件小节 {@code [tfc]}，存放各群峦饰品共用的判定参数（例如哪些实体属于「矛类投掷物」）。
 */
public final class TfcConfig {
    /** 视为「矛类投掷物」的实体类型 ID 列表。 */
    public static ModConfigSpec.ConfigValue<List<? extends String>> spearProjectileTypes;

    private TfcConfig() {
    }

    public static void init(ModConfigSpec.Builder builder) {
        builder.push("tfc");
        spearProjectileTypes = builder
                .comment("视为「矛类投掷物」的实体类型 ID 列表",
                        "默认填入群峦(TFC)的标枪实体，可按需增删")
                .defineList("spearProjectileTypes",
                        List.of("tfc:javelin"),
                        () -> "",
                        TerraConfig::isResourceLocation);
        builder.pop();
    }

    /** 判断某投掷物是否为群峦「矛类投掷物」（标枪等）。 */
    public static boolean isSpear(Projectile projectile) {
        return ProjectileUtil.matchesType(projectile, spearProjectileTypes.get());
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
