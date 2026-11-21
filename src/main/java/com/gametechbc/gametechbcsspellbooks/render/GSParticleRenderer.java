package com.gametechbc.gametechbcsspellbooks.render;

import com.gametechbc.gametechbcsspellbooks.init.GSParticles;
import com.gametechbc.gametechbcsspellbooks.particle.AbyssSpikeParticle;
import com.gametechbc.gametechbcsspellbooks.particle.AcidDropParticle;
import com.gametechbc.gametechbcsspellbooks.particle.frost_blast.FrostBlastParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(
   modid = "gametechbcs_spellbooks",
   bus = Bus.MOD,
   value = {Dist.CLIENT}
)
public class GSParticleRenderer {
   public GSParticleRenderer() {
   }

   @SubscribeEvent
   public static void registerParticles(RegisterParticleProvidersEvent event) {
      event.registerSpriteSet((ParticleType)GSParticles.FROST_BLAST.get(), FrostBlastParticle.Provider::new);
      event.registerSpriteSet((ParticleType)GSParticles.ABYSS_SPIKE_PARTICLE.get(), AbyssSpikeParticle.Provider::new);
      event.registerSpriteSet((ParticleType)GSParticles.ACID_DROP_PARTICLE.get(), AcidDropParticle.Provider::new);
   }
}
