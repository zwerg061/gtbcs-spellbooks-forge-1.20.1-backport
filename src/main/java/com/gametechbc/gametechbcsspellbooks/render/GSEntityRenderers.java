package com.gametechbc.gametechbcsspellbooks.render;

import com.gametechbc.gametechbcsspellbooks.entity.spells.psychic_bolt.PsychicBoltRenderer;
import com.gametechbc.gametechbcsspellbooks.entity.spells.reversal.ReversalRenderer;
import com.gametechbc.gametechbcsspellbooks.init.GSEntities;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingRenderer;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers;

@EventBusSubscriber(
   modid = "gametechbcs_spellbooks",
   bus = Bus.MOD,
   value = {Dist.CLIENT}
)
public class GSEntityRenderers {
   public GSEntityRenderers() {
   }

   @SubscribeEvent
   public static void onRegisterLayers(RegisterLayerDefinitions event) {
      event.registerLayerDefinition(PsychicBoltRenderer.MODEL_LAYER_LOCATION, PsychicBoltRenderer::createModel);
   }

   @SubscribeEvent
   public static void registerEntityRenderers(RegisterRenderers event) {
      event.registerEntityRenderer((EntityType)GSEntities.ASHEN_BREATH_PROJECTILE.get(), NoopRenderer::new);
      event.registerEntityRenderer((EntityType)GSEntities.BLACKOUT_ANTI_MAGIC_FIELD.get(), NoopRenderer::new);
      event.registerEntityRenderer((EntityType)GSEntities.ENSNARE.get(), NoopRenderer::new);
      event.registerEntityRenderer((EntityType)GSEntities.ACID_RAIN_AOE.get(), NoopRenderer::new);
      event.registerEntityRenderer((EntityType)GSEntities.BLOOD_RAIN_AOE.get(), NoopRenderer::new);
      event.registerEntityRenderer((EntityType)GSEntities.REVERSAL.get(), ReversalRenderer::new);
      event.registerEntityRenderer((EntityType)GSEntities.EXTENDED_DEAD_KING.get(), DeadKingRenderer::new);
      event.registerEntityRenderer((EntityType)GSEntities.PSYCHIC_BOLT_PROJECTILE.get(), PsychicBoltRenderer::new);
   }
}
