package com.gametechbc.gametechbcsspellbooks.effects;

import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class DisplacementEffect extends MobEffect {
   public DisplacementEffect() {
      super(MobEffectCategory.BENEFICIAL, 6118911);
   }

   public void applyEffectTick(LivingEntity entity, int amplifier) {
      Level level = entity.level();
      if (!level.isClientSide && level instanceof ServerLevel) {
         float radius = amplifier;
         AABB area = new AABB(entity.blockPosition()).inflate(radius);
         MagicManager.spawnParticles(
            level,
            new BlastwaveParticleOptions(((SchoolType)SchoolRegistry.ENDER.get()).getTargetingColor(), radius),
            entity.getX(),
            entity.getY() + 0.165F,
            entity.getZ(),
            1,
            0.0,
            0.0,
            0.0,
            0.0,
            true
         );

         for (Entity nearbyEntity : level.getEntities(entity, area, e -> e instanceof LivingEntity)) {
            if (nearbyEntity != entity && nearbyEntity instanceof LivingEntity target) {
               this.teleportEntity(level, target);
            }
         }

         return;
      } else {
         return;
      }
   }

   public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
      return duration % 7 == 0;
   }

   private void teleportEntity(Level level, LivingEntity target) {
      this.emitRandomBodyParticles((ServerLevel)level, target, 50);
      Vec3 targetLookDirection = target.getLookAngle().scale(-1.0);
      Vec3 teleportCenter = target.position().add(targetLookDirection.scale(5.0));
      double randomXOffset = (level.random.nextDouble() - 0.5) * 7.0;
      double randomZOffset = (level.random.nextDouble() - 0.5) * 7.0;
      Vec3 teleportPosition = teleportCenter.add(randomXOffset, 0.0, randomZOffset);
      target.teleportTo(teleportPosition.x, teleportPosition.y, teleportPosition.z);
      this.emitRandomBodyParticles((ServerLevel)level, target, 50);
   }

   private void emitRandomBodyParticles(ServerLevel serverLevel, LivingEntity entity, int count) {
      for (int i = 0; i < count; i++) {
         double offsetX = (serverLevel.random.nextDouble() - 0.5) * entity.getBbWidth();
         double offsetY = serverLevel.random.nextDouble() * entity.getBbHeight();
         double offsetZ = (serverLevel.random.nextDouble() - 0.5) * entity.getBbWidth();
         serverLevel.sendParticles(
            ParticleHelper.UNSTABLE_ENDER, entity.getX() + offsetX, entity.getY() + offsetY, entity.getZ() + offsetZ, 1, 0.0, 0.0, 0.0, 0.0
         );
      }
   }
}
