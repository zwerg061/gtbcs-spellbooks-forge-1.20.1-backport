package com.gametechbc.gametechbcsspellbooks.particle.frost_blast;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.HugeExplosionParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FrostBlastParticle extends HugeExplosionParticle {
   protected FrostBlastParticle(ClientLevel level, double x, double y, double z, double velocity, SpriteSet spriteSet) {
      super(level, x, y, z, velocity, spriteSet);
      this.lifetime = 10;
      this.quadSize = 1.2F;
      this.setSpriteFromAge(spriteSet);
   }

   @OnlyIn(Dist.CLIENT)
   public static class Provider implements ParticleProvider<SimpleParticleType> {
      private final SpriteSet sprites;

      public Provider(SpriteSet spriteSet) {
         this.sprites = spriteSet;
      }

      public Particle createParticle(
         SimpleParticleType type, ClientLevel level, double x, double y, double z, double xVelocity, double yVelocity, double zVelocity
      ) {
         return new FrostBlastParticle(level, x, y, z, xVelocity, this.sprites);
      }
   }
}
