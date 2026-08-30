package com.terramc.tm.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.terramc.tm.TerraMC;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * JSON 数值配置（数据驱动），替代按饰品编写 ModConfigSpec 配置类的做法。
 * <p>
 * 目录约定：{@code data/tm/config/<联动模组id>/<分类>.json}，例如
 * {@code data/tm/config/tfc/accessories.json}。文件内容以「key -> 字段表」组织：
 * <pre>
 * {
 *   "tfc_primal_intuition": { "enabled": true, "thrownDamageBonus": 4.0 },
 *   ...
 * }
 * </pre>
 * <p>
 * 读取：{@code JsonConfig.getBoolean("tfc/accessories", "tfc_primal_intuition", "enabled", true)}，
 * 即「config 后的路径 + key + 字段 + 缺省值」。效果类推荐通过 {@link #bool}/{@link #intValue}/
 * {@link #doubleValue}/{@link #stringList} 工厂在构造时注入惰性句柄（{@link Supplier}），
 * 与旧的 ModConfigSpec 值对象用法一致（调用 {@code get()}）。
 * <p>
 * 加载时机：随数据包重载（进入世界/数据包变更时），因此<b>只在服务端可用</b>；
 * 文件缺失、key/字段缺失或类型不符时一律回退调用方给出的默认值——
 * JSON 文件是覆盖手段，默认值以代码为准（两处应保持一致）。
 * <p>
 * 条件加载：文件本身只被本类读取；联动模组未安装时不会有任何代码去请求对应路径，
 * 无需额外条件字段，符合「不装联动模组就不加载其内容」的理念。
 */
public final class JsonConfig {
    private static final Gson GSON = new Gson();

    /** path -> { key -> { field -> value } }，随数据包重载整体替换为不可变快照。 */
    private static volatile Map<String, JsonObject> data = Map.of();

    private JsonConfig() {
    }

    /** 在模组构造期调用：注册数据包重载监听（{@link AddReloadListenerEvent} 在游戏总线发布）。 */
    public static void register() {
        NeoForge.EVENT_BUS.addListener((AddReloadListenerEvent event) -> event.addListener(LISTENER));
    }

    // ===== 类型化读取 =====

    public static boolean getBoolean(String path, String key, String field, boolean def) {
        JsonElement value = lookup(path, key, field);
        return value != null && value.isJsonPrimitive() ? value.getAsBoolean() : def;
    }

    public static int getInt(String path, String key, String field, int def) {
        JsonElement value = lookup(path, key, field);
        return value != null && value.isJsonPrimitive() ? value.getAsInt() : def;
    }

    public static double getDouble(String path, String key, String field, double def) {
        JsonElement value = lookup(path, key, field);
        return value != null && value.isJsonPrimitive() ? value.getAsDouble() : def;
    }

    public static String getString(String path, String key, String field, String def) {
        JsonElement value = lookup(path, key, field);
        return value != null && value.isJsonPrimitive() ? value.getAsString() : def;
    }

    public static List<String> getStringList(String path, String key, String field, List<String> def) {
        JsonElement value = lookup(path, key, field);
        if (value == null || !value.isJsonArray()) {
            return def;
        }
        List<String> list = new ArrayList<>();
        for (JsonElement item : value.getAsJsonArray()) {
            if (item.isJsonPrimitive()) {
                list.add(item.getAsString());
            }
        }
        return List.copyOf(list);
    }

    // ===== Supplier 工厂（效果构造器注入用，惰性求值，数据包 /reload 后自动生效） =====

    public static Supplier<Boolean> bool(String path, String key, String field, boolean def) {
        return () -> getBoolean(path, key, field, def);
    }

    public static Supplier<Integer> intValue(String path, String key, String field, int def) {
        return () -> getInt(path, key, field, def);
    }

    public static Supplier<Double> doubleValue(String path, String key, String field, double def) {
        return () -> getDouble(path, key, field, def);
    }

    public static Supplier<List<String>> stringList(String path, String key, String field, List<String> def) {
        return () -> getStringList(path, key, field, def);
    }

    private static JsonElement lookup(String path, String key, String field) {
        JsonObject section = data.get(path);
        if (section == null) {
            return null;
        }
        if (!(section.get(key) instanceof JsonObject entry)) {
            return null;
        }
        return entry.get(field);
    }

    /**
     * 扫描所有命名空间的 {@code config/} 目录，只保留 {@code tm} 命名空间的文件。
     * 键形如 {@code tm:config/tfc/accessories(.json)}，归一化为路径 {@code tfc/accessories}。
     */
    private static final SimpleJsonResourceReloadListener LISTENER = new SimpleJsonResourceReloadListener(GSON, "config") {
        @Override
        protected void apply(Map<ResourceLocation, JsonElement> files, ResourceManager resourceManager, ProfilerFiller profiler) {
            Map<String, JsonObject> loaded = new HashMap<>();
            for (Map.Entry<ResourceLocation, JsonElement> entry : files.entrySet()) {
                ResourceLocation id = entry.getKey();
                if (!TerraMC.MODID.equals(id.getNamespace())) {
                    continue;
                }
                String path = id.getPath();
                if (path.startsWith("config/")) {
                    path = path.substring("config/".length());
                }
                if (path.endsWith(".json")) {
                    path = path.substring(0, path.length() - ".json".length());
                }
                if (entry.getValue() instanceof JsonObject obj) {
                    loaded.put(path, obj);
                }
            }
            data = Map.copyOf(loaded);
            TerraMC.LOGGER.info("[TerraMC] 已加载 {} 个 JSON 数值配置文件: {}", loaded.size(), loaded.keySet());
        }
    };
}
