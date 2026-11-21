package com.gametechbc.gametechbcsspellbooks.util;

import io.redspace.ironsspellbooks.particle.FogParticleOptions;
import io.redspace.ironsspellbooks.particle.SparkParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import org.joml.Vector3f;

public class GSParticleHelper {
   public static final ParticleOptions PSYCHIC_SPARKS = new SparkParticleOptions(new Vector3f(0.1608F, 0.8745F, 0.9216F));
   public static final ParticleOptions CRIMSON_CLOUD = new FogParticleOptions(new Vector3f(0.8F, 0.1F, 0.1F), 2.0F);
   public static final ParticleOptions BLOOD_CLOUD = new FogParticleOptions(new Vector3f(0.6F, 0.0F, 0.0F), 2.0F);

   public GSParticleHelper() {
   }
}
