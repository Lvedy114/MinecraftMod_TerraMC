package com.terramc.tm.mixin;

import com.terramc.tm.accessory.AccessoryItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

/**
 * 拦截汇流来世额外栏的 {@code ToggleCurioSlot.mayPlace}，添加与 Curios 主栏位一致的互斥检查。
 * 由于汇流来世额外栏直接通过 {@code curios:accessory} 标签判定是否可放入，未经过 Curios 的
 * {@code DynamicStackHandler.isItemValid} → {@code CurioCanEquipEvent} 链路，
 * 因此需要单独在此处补充同款/同组互斥。
 */
@Mixin(targets = "org.confluence.mod.common.menu.ToggleCurioSlot")
public abstract class ToggleCurioSlotMixin {

    @Accessor("player")
    abstract Player getPlayer();

    /**
     * 在 {@code mayPlace} 返回 {@code true} 后，追加 TerraMC 的互斥检查：
     * 同款不可重复装备，同组只能装备一件。
     */
    @Inject(method = "mayPlace", at = @At("RETURN"), cancellable = true)
    private void onMayPlace(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            return;
        }
        if (!(stack.getItem() instanceof AccessoryItem accessory)) {
            return;
        }
        Player player = getPlayer();
        if (hasConflict(player, stack, accessory)) {
            cir.setReturnValue(false);
        }
    }

    private static boolean hasConflict(Player player, ItemStack candidate, AccessoryItem accessory) {
        return CuriosApi.getCuriosInventory(player).map(handler -> {
            for (ICurioStacksHandler sh : handler.getCurios().values()) {
                var stacks = sh.getStacks();
                for (int i = 0; i < stacks.getSlots(); i++) {
                    ItemStack equipped = stacks.getStackInSlot(i);
                    if (equipped.isEmpty()) {
                        continue;
                    }
                    if (equipped.getItem() == candidate.getItem()) {
                        return true;
                    }
                    if (equipped.getItem() instanceof AccessoryItem other
                            && accessory.group().equals(other.group())) {
                        return true;
                    }
                }
            }
            return false;
        }).orElse(false);
    }
}