package com.gametechbc.gametechbcsspellbooks.init;

import java.util.function.Supplier;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

public class GSParticles {
   public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, "gametechbcs_spellbooks");
   public static final Supplier<SimpleParticleType> FROST_BLAST = PARTICLE_TYPES.register("frost_blast", () -> new SimpleParticleType(true));
   public static final Supplier<SimpleParticleType> ABYSS_SPIKE_PARTICLE = PARTICLE_TYPES.register("abyss_spike", () -> new SimpleParticleType(false));
   public static final Supplier<SimpleParticleType> ACID_DROP_PARTICLE = PARTICLE_TYPES.register("acid_drop", () -> new SimpleParticleType(false));

   public GSParticles() {
   }

   public static void register(IEventBus eventBus) {
      PARTICLE_TYPES.register(eventBus);
   }
}
