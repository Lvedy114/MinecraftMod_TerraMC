package com.terramc.tm.compat.tfc.client;

import com.terramc.tm.TerraMC;
import com.terramc.tm.compat.Integrations;
import com.terramc.tm.compat.tfc.client.renderer.RadiantStarProjectileRenderer;
import com.terramc.tm.compat.tfc.client.renderer.StoneClubProjectileRenderer;
import com.terramc.tm.compat.tfc.TfcEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = TerraMC.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class TfcEntityRenderers {
    private TfcEntityRenderers() {
    }

    @SubscribeEvent
    public static void register(EntityRenderersEvent.RegisterRenderers event) {
        if (Integrations.isTfc()) {
            event.registerEntityRenderer(TfcEntities.STONE_CLUB_PROJECTILE.get(), StoneClubProjectileRenderer::new);
            event.registerEntityRenderer(TfcEntities.RADIANT_STAR_PROJECTILE.get(), RadiantStarProjectileRenderer::new);
        }
    }
}
