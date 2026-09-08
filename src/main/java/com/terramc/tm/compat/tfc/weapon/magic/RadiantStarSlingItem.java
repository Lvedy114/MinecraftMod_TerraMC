package com.terramc.tm.compat.tfc.weapon.magic;

import com.terramc.tm.compat.tfc.weapon.entity.RadiantStarProjectile;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.confluence.lib.common.component.ModRarity;
import org.confluence.mod.common.init.item.MaterialItems;
import org.confluence.mod.common.item.mana.ManaStaffItem;
import org.confluence.mod.util.PlayerUtils;
import org.confluence.mod.util.PrefixUtils;

import java.util.List;

/**
 * 辉星投石索：以坠落之星为弹药的法师武器。
 * <p>
 * 直接继承汇流来世 {@link ManaStaffItem}，复用法师武器专属属性体系：
 * <ul>
 *     <li>射弹速度：rawVelocity = 7.6F（基类内部换算 7.6 / 8 = 0.95，tooltip
 *     由基类 {@code appendHoverText} 自动显示“射弹速度：0.95”，这是法师武器独有的属性，
 *     不需要也不应该在射手武器上重复实现）；</li>
 *     <li>伤害、冷却、手持暴击率（+4%）均走基类构造参数与 LibAttributes 属性修饰器；</li>
 *     <li>发射、射线检测（近距防穿模）与射击音效全部复用基类 {@code use} 流程。</li>
 * </ul>
 * 弹药机制为投石索特有：1 颗坠落之星 + 100 魔力装填 10 发；弹药数写入
 * {@code CUSTOM_DATA} 数据组件，并以耐久条显示（满弹药 100%，空弹药 2%，永不真正损坏）。
 */
public class RadiantStarSlingItem extends ManaStaffItem<RadiantStarProjectile> {
    private static final String WEAPONS = "tfc/weapons";

    /** 每次装填消耗的坠落之星数量。 */
    private static final int RELOAD_STAR_COST = 1;
    /** 每次装填消耗的魔力（走 PrefixUtils 前缀与附魔减免管线）。 */
    private static final int RELOAD_MANA_COST = 100;
    /** 满弹药可发射的星弹数。 */
    private static final int MAX_AMMO = 10;
    /** 弹药在 CUSTOM_DATA 中的键。 */
    private static final String AMMO_KEY = "AmmoCount";
    /** 空仓时耐久条显示的最低比例（2%）。 */
    private static final float MIN_DURABILITY = 0.02F;

    public RadiantStarSlingItem() {
        // damage=10, manaCost=100(装填消耗), rawVelocity=7.6F(射弹速度0.95), cooldown=20(1秒), critChance=+4%
        super(ModRarity.BLUE, RadiantStarProjectile::new, 10.0F, RELOAD_MANA_COST, 7.6F, 20, 0.04);
    }

    /** 根据弹药状态切换：有弹药正常发射（复用基类），空仓尝试装填。 */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (getAmmo(stack) > 0) {
            return super.use(level, player, usedHand);
        }
        if (player instanceof ServerPlayer serverPlayer) {
            reload(serverPlayer, stack);
        }
        return InteractionResultHolder.success(stack);
    }

    /** 射击本身不消耗魔力（魔力在装填时结算），只需有弹药。 */
    @Override
    protected boolean couldShoot(ServerPlayer player, ItemStack stack) {
        return getAmmo(stack) > 0;
    }

    @Override
    protected void afterShoot(ServerPlayer player, ItemStack stack, RadiantStarProjectile projectile) {
        super.afterShoot(player, stack, projectile);
        setAmmo(stack, getAmmo(stack) - 1);
    }

    /** 空时装填：1 颗坠落之星 + 100 魔力 = 10 发弹药；魔力不足时不消耗坠落之星。 */
    private void reload(ServerPlayer player, ItemStack stack) {
        if (player.isCreative()) {
            setAmmo(stack, MAX_AMMO);
            return;
        }
        int slot = player.getInventory().findSlotMatchingItem(new ItemStack(MaterialItems.FALLING_STAR.get()));
        if (slot < 0) {
            return;
        }
        if (!PlayerUtils.extractMana(player, stack, () -> PrefixUtils.calculateManaCost(stack, RELOAD_MANA_COST))) {
            return;
        }
        player.getInventory().removeItem(slot, RELOAD_STAR_COST);
        setAmmo(stack, MAX_AMMO);
        player.level().playSound(null, player.getX(), player.getEyeY(), player.getZ(),
                SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.0F, 1.2F);
    }

    // ===== 弹药存取（CUSTOM_DATA 数据组件为唯一数据源） =====

    public static int getAmmo(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data == null ? 0 : data.copyTag().getInt(AMMO_KEY);
    }

    public static void setAmmo(ItemStack stack, int ammo) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt(AMMO_KEY, ammo));
    }

    // ===== 耐久条即弹药表（满=100%，空=2%，不参与真实损坏） =====

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getAmmo(stack) < MAX_AMMO;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        float durability = Math.max(MIN_DURABILITY, getAmmo(stack) / (float) MAX_AMMO);
        return Math.round(13.0F * durability);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0xF5C542;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(Component.translatable("tooltip.item.tm.tfc_radiant_star_sling.0").withStyle(ChatFormatting.GRAY));
    }
}
