package com.terramc.tm.accessory;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;

/**
 * 饰品管理器：负责「玩家是否装备某饰品」的判定，以及「取出玩家当前装备的饰品」。
 * <p>
 * 装备判定通过 Curios API 实现（与汇流来世一致）：查询玩家的 Curios 饰品栏位中是否装有对应饰品。
 */
public final class AccessoryManager {
    private AccessoryManager() {
    }

    /** 判断玩家是否在 Curios 饰品栏位中装备了指定 ID 的饰品。 */
    public static boolean isEquipped(Player player, ResourceLocation accessoryId) {
        return CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.isEquipped(stack -> AccessoryItem.isAccessory(stack, accessoryId)))
                .orElse(false);
    }

    /** 返回玩家当前装备的所有饰品定义。 */
    public static List<AccessoryDefinition> equipped(Player player) {
        return AccessoryRegistry.all().stream()
                .filter(definition -> isEquipped(player, definition.id()))
                .toList();
    }
}
