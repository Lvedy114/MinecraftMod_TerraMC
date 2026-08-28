package com.terramc.tm.compat.tfc.effects;

import com.terramc.tm.TerraMC;
import com.terramc.tm.accessory.AccessoryEffect;
import com.terramc.tm.compat.tfc.config.TfcConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * 灵动步法：每装备一件群峦护甲，获得一定比例的移动速度加成。
 */
public class ArmorSpeed extends AccessoryEffect {
    private static final ResourceLocation MODIFIER_ID = TerraMC.id("tfc_nimble_footwork_speed");

    private final ModConfigSpec.BooleanValue enabled;
    private final ModConfigSpec.DoubleValue speedPerArmor;

    public ArmorSpeed(ModConfigSpec.BooleanValue enabled, ModConfigSpec.DoubleValue speedPerArmor) {
        this.enabled = enabled;
        this.speedPerArmor = speedPerArmor;
    }

    @Override
    public void onPlayerTick(ServerPlayer player) {
        AttributeInstance attr = player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
        if (!enabled.get()) {
            attr.removeModifier(MODIFIER_ID);
            return;
        }
        double value = TfcConfig.countTfcArmor(player) * speedPerArmor.get();
        AttributeModifier existing = attr.getModifier(MODIFIER_ID);
        if (existing != null && existing.amount() == value) {
            return;
        }
        attr.addOrUpdateTransientModifier(new AttributeModifier(MODIFIER_ID, value, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    }

    @Override
    public void onUnequip(ServerPlayer player) {
        player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).removeModifier(MODIFIER_ID);
    }
}
