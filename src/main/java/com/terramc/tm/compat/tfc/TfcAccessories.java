package com.terramc.tm.compat.tfc;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.terramc.tm.TerraMC;
import com.terramc.tm.accessory.AccessoryItem;
import com.terramc.tm.compat.tfc.Helper.ModAttributeHelper;
import com.terramc.tm.compat.tfc.config.*;
import com.terramc.tm.compat.tfc.effects.ArmorDefense;
import com.terramc.tm.compat.tfc.effects.ArmorSpeed;
import com.terramc.tm.compat.tfc.effects.NoArmorDurability;
import com.terramc.tm.compat.tfc.effects.Tempered;
import com.terramc.tm.compat.tfc.effects.ThermalCore;
import com.terramc.tm.compat.tfc.effects.javelin.SpearRecallEffect;
import com.terramc.tm.compat.tfc.effects.javelin.ThrownSpearDamageBonusEffect;
import com.terramc.tm.compat.tfc.effects.javelin.ThrownSpearGlowEffect;
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
 */
public final class TfcAccessories {
    // ===== 矛类组 =====
    private static final ResourceLocation SPEAR_GROUP = TerraMC.id("tfc_spear");

    public static final DeferredItem<AccessoryItem> PRIMAL_INTUITION = ModItems.ITEMS.register(
            "tfc_primal_intuition",
            () -> new AccessoryItem(new Item.Properties().stacksTo(1), SPEAR_GROUP,
                    new ThrownSpearGlowEffect(TfcPrimalIntuitionConfig.enabled, TfcPrimalIntuitionConfig.glowEnabled),
                    new ThrownSpearDamageBonusEffect(TfcPrimalIntuitionConfig.enabled, TfcPrimalIntuitionConfig.thrownDamageBonus)));

    public static final DeferredItem<AccessoryItem> PRACTICE_MAKES_PERFECT = ModItems.ITEMS.register(
            "tfc_practice_makes_perfect",
            () -> new AccessoryItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON), SPEAR_GROUP,
                    new ThrownSpearGlowEffect(TfcPracticeMakesPerfectConfig.enabled, TfcPracticeMakesPerfectConfig.glowEnabled),
                    new ThrownSpearDamageBonusEffect(TfcPracticeMakesPerfectConfig.enabled, TfcPracticeMakesPerfectConfig.thrownDamageBonus),
                    new SpearRecallEffect(TfcPracticeMakesPerfectConfig.enabled, TfcPracticeMakesPerfectConfig.recallRange)));

    public static final DeferredItem<AccessoryItem> DIVINE_SKILL = ModItems.ITEMS.register(
            "tfc_divine_skill",
            () -> new AccessoryItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE), SPEAR_GROUP,
                    new ThrownSpearGlowEffect(TfcDivineSkillConfig.enabled, TfcDivineSkillConfig.glowEnabled),
                    new ThrownSpearDamageBonusEffect(TfcDivineSkillConfig.enabled, TfcDivineSkillConfig.thrownDamageBonus),
                    new SpearRecallEffect(TfcDivineSkillConfig.enabled, TfcDivineSkillConfig.recallRange, TfcDivineSkillConfig.recallPathDamage)));

    // ===== 护甲/战斗类 =====

    public static final DeferredItem<AccessoryItem> NIMBLE_FOOTWORK = ModItems.ITEMS.register(
            "tfc_nimble_footwork",
            () -> new AccessoryItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                    new ArmorSpeed(TfcNimbleFootworkConfig.enabled, TfcNimbleFootworkConfig.speedPerArmor)));

    public static final DeferredItem<AccessoryItem> SURVIVOR = ModItems.ITEMS.register(
            "tfc_survivor",
            () -> new AccessoryItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                    new ArmorDefense(TfcSurvivorConfig.enabled, TfcSurvivorConfig.armorPerArmor, TfcSurvivorConfig.toughnessPerArmor)));

    public static final DeferredItem<AccessoryItem> EARLY_PREPARATION = ModItems.ITEMS.register(
            "tfc_early_preparation",
            () -> new AccessoryItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE),
                    new NoArmorDurability(TfcEarlyPreparationConfig.enabled)));

    public static final DeferredItem<AccessoryItem> EARLY_PREPARATION_PRO = ModItems.ITEMS.register(
            "tfc_early_preparation_pro",
            () -> new AccessoryItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                    new NoArmorDurability(TfcEarlyPreparationConfig.enabled),
                    new ArmorSpeed(TfcNimbleFootworkConfig.enabled, TfcNimbleFootworkConfig.speedPerArmor),
                    new ArmorDefense(TfcSurvivorConfig.enabled, TfcSurvivorConfig.armorPerArmor, TfcSurvivorConfig.toughnessPerArmor)
            ));

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
                                        TfcPrimalStrengthConfig.attackDamage.get(),
                                        AttributeModifier.Operation.ADD_VALUE,
                                        ResourceLocation.parse("tm:primal_strength_damage"),
                                        "Primal Damage"
                                ).get()
                        );
                        modifiers.putAll(
                                ModAttributeHelper.simpleModifier(
                                        Attributes.ATTACK_SPEED,
                                        TfcPrimalStrengthConfig.attackSpeedMultiplier.get(),
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
                    new ThermalCore(TfcThermalCoreConfig.enabled, TfcThermalCoreConfig.requiredFuel, TfcThermalCoreConfig.healAmount, TfcThermalCoreConfig.speedTicks, TfcThermalCoreConfig.cooldownTicks)));

    public static final DeferredItem<AccessoryItem> THERMAL_CORE_PRO = ModItems.ITEMS.register(
            "tfc_thermal_core_pro",
            () -> new AccessoryItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE),
                    new ThermalCore(TfcThermalCoreProConfig.enabled, TfcThermalCoreProConfig.requiredFuel, TfcThermalCoreProConfig.healAmount, TfcThermalCoreProConfig.speedTicks, TfcThermalCoreConfig.cooldownTicks)));

    public static final DeferredItem<AccessoryItem> TEMPERED = ModItems.ITEMS.register(
            "tfc_tempered",
            () -> new AccessoryItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE),
                    new Tempered()));

    private TfcAccessories() {
    }

    public static void init() {
    }
}

