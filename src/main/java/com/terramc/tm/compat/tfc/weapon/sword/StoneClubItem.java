package com.terramc.tm.compat.tfc.weapon.sword;

import com.terramc.tm.compat.tfc.weapon.component.StoneClubProjectileComponent;
import com.terramc.tm.compat.tfc.weapon.entity.StoneClubProjectile;
import com.terramc.tm.compat.tfc.TfcEntities;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.confluence.lib.common.component.ModRarity;
import org.confluence.mod.common.component.SwordProjectileComponent;
import org.confluence.mod.common.item.sword.BaseSwordItem;
import org.confluence.mod.common.item.sword.legacy.SwordPrefabs;

/**
 * 石头做的棒子：继承汇流来世 BaseSwordItem，挥动时向前发射一枚不断翻滚的石头弹幕。
 */
public final class StoneClubItem extends BaseSwordItem {
    private static final float PROJECTILE_SPEED = 1.25F;

    public StoneClubItem(Tier tier, ModRarity rarity, int damage, float speed) {
        super(tier, rarity, damage, speed,
                SwordPrefabs.PROJ_SWORD.apply(StoneClubProjectileComponent.create())
                        .addTooltip(p -> p.withColor(0x777777)));
    }

    @Override
    public void genProjectile(LivingEntity living, ItemStack weapon, SwordProjectileComponent data) {
        Level level = living.level();
        if (level.isClientSide) {
            return;
        }

        Vec3 direction = living.getLookAngle().normalize();
        StoneClubProjectile projectile = TfcEntities.STONE_CLUB_PROJECTILE.get().create(level);
        if (projectile == null) {
            return;
        }

        projectile.launch(
                living,
                living.getEyePosition().add(direction.scale(0.35D)),
                direction.scale(PROJECTILE_SPEED));
        projectile.setWeapon(weapon.copy());
        level.addFreshEntity(projectile);
    }
}
