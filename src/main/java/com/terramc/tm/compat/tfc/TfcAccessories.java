package com.terramc.tm.compat.tfc;

import com.terramc.tm.TerraMC;
import com.terramc.tm.accessory.AccessoryDefinition;
import com.terramc.tm.accessory.AccessoryItem;
import com.terramc.tm.accessory.AccessoryRegistry;
import com.terramc.tm.accessory.effect.ThrownSpearDamageBonusEffect;
import com.terramc.tm.accessory.effect.ThrownSpearGlowEffect;
import com.terramc.tm.init.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

/**
 * 群峦(TerraFirmaCraft / TFC)联动饰品的注册入口。
 * <p>
 * 为每个联动模组在 {@code compat/&lt;modid&gt;} 下建一个类似本类的入口，集中注册该模组的专属饰品。
 */
public final class TfcAccessories {
    /** 示例饰品「[群峦]原始直觉」的饰品 ID。 */
    public static final ResourceLocation PRIMAL_INTUITION = TerraMC.id("tfc_primal_intuition");

    /** 示例饰品物品（注册名 tm:tfc_primal_intuition）。 */
    public static final DeferredItem<AccessoryItem> PRIMAL_INTUITION_ITEM = ModItems.ITEMS.register(
            "tfc_primal_intuition",
            () -> new AccessoryItem(PRIMAL_INTUITION, new Item.Properties().stacksTo(1)));

    private TfcAccessories() {
    }

    /** 在模组初始化阶段调用（见 {@code TerraMC} 构造器），注册本联动模组的全部饰品定义。 */
    public static void register() {
        AccessoryRegistry.register(new AccessoryDefinition(
                PRIMAL_INTUITION,
                PRIMAL_INTUITION_ITEM,
                new ThrownSpearGlowEffect("glow"),
                new ThrownSpearDamageBonusEffect("thrown_damage_bonus")
        ));
    }
}
