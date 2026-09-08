package com.terramc.tm.compat.tfc;

import com.terramc.tm.TerraMC;
import com.terramc.tm.compat.Integrations;
import com.terramc.tm.compat.tfc.weapon.entity.RadiantStarProjectile;
import com.terramc.tm.compat.tfc.weapon.entity.StoneClubProjectile;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** TFC 联动实体注册入口。该注册中心只在 TFC 已加载时挂载。 */
public final class TfcEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, TerraMC.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<StoneClubProjectile>> STONE_CLUB_PROJECTILE =
            ENTITIES.register("stone_club_projectile", id -> EntityType.Builder
                    .of(StoneClubProjectile::new, MobCategory.MISC)
                    .sized(0.28F, 0.28F)
                    .clientTrackingRange(8)
                    .updateInterval(1)
                    .build(id.toString()));

    /** 辉星投石索的星弹弹幕（参数对齐汇流法杖弹幕：追踪范围 10，曲线弹道按 tick 同步）。 */
    public static final DeferredHolder<EntityType<?>, EntityType<RadiantStarProjectile>> RADIANT_STAR_PROJECTILE =
            ENTITIES.register("radiant_star_projectile", id -> EntityType.Builder
                    .<RadiantStarProjectile>of(RadiantStarProjectile::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(10)
                    .updateInterval(1)
                    .build(id.toString()));

    public static void init() {
        if (!Integrations.isTfc()) {
            throw new IllegalStateException("TfcEntities loaded without TFC");
        }
    }

    private TfcEntities() {
    }
}
