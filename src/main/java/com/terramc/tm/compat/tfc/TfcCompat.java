package com.terramc.tm.compat.tfc;

import net.neoforged.bus.api.IEventBus;

/**
 * 群峦(TerraFirmaCraft)联动统一入口。
 * <p>
 * 主类 {@code TerraMC} 只在 {@code Integrations.isTfc()} 为真时调用 {@link #init(IEventBus)}；
 * 该联动的清单类加载、事件总线挂载全部在此收口，主类无需了解联动内部结构。
 * <p>
 * 新增其它联动时照此模式建立 {@code compat/<modid>/<Modid>Compat} 入口，
 * 保持主类每个联动只有一行初始化调用。
 */
public final class TfcCompat {

    private TfcCompat() {
    }

    /**
     * 初始化 TFC 联动内容。
     * <p>
     * init() 的作用是显式触发清单类加载（其静态字段完成注册）；
     * {@link TfcEntities#ENTITIES} 是 TFC 专属实体注册中心，只在此处挂载到模组事件总线。
     */
    public static void init(IEventBus modEventBus) {
        TfcAccessories.init();
        TfcWeapon.init();
        TfcEntities.init();
        TfcEntities.ENTITIES.register(modEventBus);
    }
}
