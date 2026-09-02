package com.terramc.tm.compat.tfc;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.terramc.tm.TerraMC;
import com.terramc.tm.accessory.AccessoryItem;
import com.terramc.tm.compat.tfc.Helper.ModAttributeHelper;
import com.terramc.tm.compat.tfc.effects.accessory.ArmorDefense;
import com.terramc.tm.compat.tfc.effects.accessory.ArmorSpeed;
import com.terramc.tm.compat.tfc.effects.accessory.NoArmorDurability;
import com.terramc.tm.compat.tfc.effects.accessory.Tempered;
import com.terramc.tm.compat.tfc.effects.accessory.ThermalCore;
import com.terramc.tm.compat.tfc.effects.accessory.javelin.SpearRecallEffect;
import com.terramc.tm.compat.tfc.effects.accessory.javelin.ThrownSpearDamageBonusEffect;
import com.terramc.tm.compat.tfc.effects.accessory.javelin.ThrownSpearGlowEffect;
import com.terramc.tm.config.JsonConfig;
import com.terramc.tm.init.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;

/**
 * 群峦(TerraFirmaCraft / TFC)联动饰品的注册入口。
 * <p>
 * 数值配置为 JSON 数据驱动（{@code data/tm/config/tfc/accessories.json}）：
 * 以饰品注册名为 key，通过 {@link JsonConfig} 工厂注入惰性句柄给效果，
 * 缺省值与 JSON 文件保持一致。
 */
public final class TfcAccessories {
    /** 配饰数值在 JSON 配置中的路径。 */
    private static final String ACCESSORIES = "tfc/accessories";

    // ===== 矛类组 =====
    private static final ResourceLocation SPEAR_GROUP = TerraMC.id("tfc_spear");

    public static final DeferredItem<AccessoryItem> PRIMAL_INTUITION = ModItems.ITEMS.register(
            "tfc_primal_intuition",
            () -> new AccessoryItem(new Item.Properties().stacksTo(1), SPEAR_GROUP,
                    new ThrownSpearGlowEffect(
                            JsonConfig.bool(ACCESSORIES, "tfc_primal_intuition", "enabled", true),
                            JsonConfig.bool(ACCESSORIES, "tfc_primal_intuition", "glowEnabled", true)),
                    new ThrownSpearDamageBonusEffect(
                            JsonConfig.bool(ACCESSORIES, "tfc_primal_intuition", "enabled", true),
                            JsonConfig.doubleValue(ACCESSORIES, "tfc_primal_intuition", "thrownDamageBonus", 4.0))));

    public static final DeferredItem<AccessoryItem> PRACTICE_MAKES_PERFECT = ModItems.ITEMS.register(
            "tfc_practice_makes_perfect",
            () -> new AccessoryItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON), SPEAR_GROUP,
                    new ThrownSpearGlowEffect(
                            JsonConfig.bool(ACCESSORIES, "tfc_practice_makes_perfect", "enabled", true),
                            JsonConfig.bool(ACCESSORIES, "tfc_practice_makes_perfect", "glowEnabled", true)),
                    new ThrownSpearDamageBonusEffect(
                            JsonConfig.bool(ACCESSORIES, "tfc_practice_makes_perfect", "enabled", true),
                            JsonConfig.doubleValue(ACCESSORIES, "tfc_practice_makes_perfect", "thrownDamageBonus", 4.0)),
                    new SpearRecallEffect(
                            JsonConfig.bool(ACCESSORIES, "tfc_practice_makes_perfect", "enabled", true),
                            JsonConfig.doubleValue(ACCESSORIES, "tfc_practice_makes_perfect", "recallRange", 15.0),
                            JsonConfig.intValue(ACCESSORIES, "tfc_practice_makes_perfect", "recallCooldownTicks", 200))));

    public static final DeferredItem<AccessoryItem> DIVINE_SKILL = ModItems.ITEMS.register(
            "tfc_divine_skill",
            () -> new AccessoryItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE), SPEAR_GROUP,
                    new ThrownSpearGlowEffect(
                            JsonConfig.bool(ACCESSORIES, "tfc_divine_skill", "enabled", true),
                            JsonConfig.bool(ACCESSORIES, "tfc_divine_skill", "glowEnabled", true)),
                    new ThrownSpearDamageBonusEffect(
                            JsonConfig.bool(ACCESSORIES, "tfc_divine_skill", "enabled", true),
                            JsonConfig.doubleValue(ACCESSORIES, "tfc_divine_skill", "thrownDamageBonus", 5.0)),
                    new SpearRecallEffect(
                            JsonConfig.bool(ACCESSORIES, "tfc_divine_skill", "enabled", true),
                            JsonConfig.doubleValue(ACCESSORIES, "tfc_divine_skill", "recallRange", 25.0),
                            JsonConfig.intValue(ACCESSORIES, "tfc_divine_skill", "recallCooldownTicks", 200),
                            JsonConfig.doubleValue(ACCESSORIES, "tfc_divine_skill", "recallPathDamage", 1.0))));

    // ===== 护甲/战斗类 =====

    public static final DeferredItem<AccessoryItem> NIMBLE_FOOTWORK = ModItems.ITEMS.register(
            "tfc_nimble_footwork",
            () -> new AccessoryItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                    new ArmorSpeed(
                            JsonConfig.bool(ACCESSORIES, "tfc_nimble_footwork", "enabled", true),
                            JsonConfig.doubleValue(ACCESSORIES, "tfc_nimble_footwork", "speedPerArmor", 0.015))));

    public static final DeferredItem<AccessoryItem> SURVIVOR = ModItems.ITEMS.register(
            "tfc_survivor",
            () -> new AccessoryItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                    new ArmorDefense(
                            JsonConfig.bool(ACCESSORIES, "tfc_survivor", "enabled", true),
                            JsonConfig.doubleValue(ACCESSORIES, "tfc_survivor", "armorPerArmor", 0.5),
                            JsonConfig.doubleValue(ACCESSORIES, "tfc_survivor", "toughnessPerArmor", 0.25))));

    public static final DeferredItem<AccessoryItem> EARLY_PREPARATION = ModItems.ITEMS.register(
            "tfc_early_preparation",
            () -> new AccessoryItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE),
                    new NoArmorDurability(JsonConfig.bool(ACCESSORIES, "tfc_early_preparation", "enabled", true))));

    public static final DeferredItem<AccessoryItem> EARLY_PREPARATION_PRO = ModItems.ITEMS.register(
            "tfc_early_preparation_pro",
            () -> new AccessoryItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                    new NoArmorDurability(JsonConfig.bool(ACCESSORIES, "tfc_early_preparation_pro", "enabled", true)),
                    new ArmorSpeed(
                            JsonConfig.bool(ACCESSORIES, "tfc_early_preparation_pro", "enabled", true),
                            JsonConfig.doubleValue(ACCESSORIES, "tfc_early_preparation_pro", "speedPerArmor", 0.015)),
                    new ArmorDefense(
                            JsonConfig.bool(ACCESSORIES, "tfc_early_preparation_pro", "enabled", true),
                            JsonConfig.doubleValue(ACCESSORIES, "tfc_early_preparation_pro", "armorPerArmor", 0.5),
                            JsonConfig.doubleValue(ACCESSORIES, "tfc_early_preparation_pro", "toughnessPerArmor", 0.25)
                    )));

    public static final DeferredItem<AccessoryItem> PRIMAL_STRENGTH = ModItems.ITEMS.register(
            "tfc_primal_strength",
            () -> new AccessoryItem(
                    new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                    null,
                    () -> {
                        Multimap<Holder<Attribute>, AttributeModifier> modifiers = ArrayListMultimap.create();
                        modifiers.putAll(
                                ModAttributeHelper.simpleModifier(
                                        Attributes.ATTACK_DAMAGE,
                                        JsonConfig.getDouble(ACCESSORIES, "tfc_primal_strength", "attackDamage", 4.0),
                                        AttributeModifier.Operation.ADD_VALUE,
                                        ResourceLocation.parse("tm:primal_strength_damage"),
                                        "Primal Damage"
                                ).get()
                        );
                        modifiers.putAll(
                                ModAttributeHelper.simpleModifier(
                                        Attributes.ATTACK_SPEED,
                                        JsonConfig.getDouble(ACCESSORIES, "tfc_primal_strength", "attackSpeedMultiplier", -0.3),
                                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                                        ResourceLocation.parse("tm:primal_strength_speed"),
                                        "Primal Speed"
                                ).get()
                        );
                        return modifiers;
                    }
            )
    );

    public static final DeferredItem<AccessoryItem> THERMAL_CORE = ModItems.ITEMS.register(
            "tfc_thermal_core",
            () -> new AccessoryItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                    new ThermalCore(
                            JsonConfig.bool(ACCESSORIES, "tfc_thermal_core", "enabled", true),
                            JsonConfig.intValue(ACCESSORIES, "tfc_thermal_core", "requiredFuel", 5),
                            JsonConfig.doubleValue(ACCESSORIES, "tfc_thermal_core", "healAmount", 4.0),
                            JsonConfig.intValue(ACCESSORIES, "tfc_thermal_core", "speedTicks", 100),
                            JsonConfig.intValue(ACCESSORIES, "tfc_thermal_core", "cooldownTicks", 200))));

    public static final DeferredItem<AccessoryItem> THERMAL_CORE_PRO = ModItems.ITEMS.register(
            "tfc_thermal_core_pro",
            () -> new AccessoryItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE),
                    new ThermalCore(
                            JsonConfig.bool(ACCESSORIES, "tfc_thermal_core_pro", "enabled", true),
                            JsonConfig.intValue(ACCESSORIES, "tfc_thermal_core_pro", "requiredFuel", 8),
                            JsonConfig.doubleValue(ACCESSORIES, "tfc_thermal_core_pro", "healAmount", 8.0),
                            JsonConfig.intValue(ACCESSORIES, "tfc_thermal_core_pro", "speedTicks", 200),
                            JsonConfig.intValue(ACCESSORIES, "tfc_thermal_core_pro", "cooldownTicks", 160))));

    public static final DeferredItem<AccessoryItem> TEMPERED = ModItems.ITEMS.register(
            "tfc_tempered",
            () -> new AccessoryItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE),
                    new Tempered()));

    private TfcAccessories() {
    }

    public static void init() {
    }
}
