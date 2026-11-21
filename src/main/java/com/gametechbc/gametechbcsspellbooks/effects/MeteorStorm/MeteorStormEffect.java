package com.gametechbc.gametechbcsspellbooks.effects.MeteorStorm;

import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.spells.fireball.MagicFireball;
import io.redspace.ironsspellbooks.particle.ShockwaveParticleOptions;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.phys.Vec3;

public class MeteorStormEffect extends MobEffect {
   private int ticksSinceLastSummon = 0;
   private static final int STATIC_OUTER_RADIUS = 24;

   public MeteorStormEffect() {
      super(MobEffectCategory.BENEFICIAL, 0);
   }

   public void applyEffectTick(LivingEntity entity, int amplifier) {
      if (!entity.level().isClientSide && entity.level() instanceof ServerLevel serverLevel) {
         this.ticksSinceLastSummon++;
         if (this.ticksSinceLastSummon >= 15) {
            this.summonProjectiles(serverLevel, entity, amplifier);
            this.ticksSinceLastSummon = 0;
         }
      }

      return;
   }

   private void summonProjectiles(ServerLevel serverLevel, LivingEntity meteorStormEntity, int amplifier) {
      RandomSource random = meteorStormEntity.getRandom();
      double innerRadius = 8.0;
      List<Entity> nearbyEntities = serverLevel.getEntities(
         meteorStormEntity,
         meteorStormEntity.getBoundingBox().inflate(24.0),
         entity -> entity instanceof LivingEntity target && !this.isTeammate(meteorStormEntity, target) && !this.isTamedCreature(meteorStormEntity, target)
      );
      Collections.shuffle(nearbyEntities, new Random(random.nextLong()));
      int numEntitiesToTarget = Math.min(nearbyEntities.size(), 2);

      for (int i = 0; i < 3; i++) {
         Vec3 spawnPosition;
         if (i < numEntitiesToTarget) {
            Entity targetEntity = nearbyEntities.get(i);
            Vec3 motion = targetEntity.getDeltaMovement();
            double predictionTime = 1.0;
            Vec3 predictedPosition = targetEntity.position().add(motion.scale(predictionTime));
            spawnPosition = new Vec3(predictedPosition.x, predictedPosition.y + 30.0, predictedPosition.z);
            MagicManager.spawnParticles(
               serverLevel,
               new ShockwaveParticleOptions(((SchoolType)SchoolRegistry.FIRE.get()).getTargetingColor(), -1.5F, true),
               predictedPosition.x,
               predictedPosition.y,
               predictedPosition.z,
               1,
               0.0,
               0.0,
               0.0,
               0.0,
               true
            );
         } else {
            double angle = random.nextDouble() * Math.PI * 2.0;
            double distance = innerRadius + random.nextDouble() * (24.0 - innerRadius);
            double xOffset = Math.cos(angle) * distance;
            double zOffset = Math.sin(angle) * distance;
            double yOffset = 30.0;
            spawnPosition = new Vec3(meteorStormEntity.getX() + xOffset, meteorStormEntity.getY() + yOffset, meteorStormEntity.getZ() + zOffset);
         }

         MagicFireball fireball = new MagicFireball(serverLevel, meteorStormEntity);
         fireball.setPos(spawnPosition);
         fireball.shoot(0.0, -1.0, 0.0, 1.8F, 0.0F);
         fireball.setDamage(amplifier);
         fireball.setExplosionRadius(3.5F);
         fireball.setOwner(meteorStormEntity);
         serverLevel.addFreshEntity(fireball);
         MagicManager.spawnParticles(serverLevel, ParticleHelper.SUNBEAM, spawnPosition.x, spawnPosition.y, spawnPosition.z, 1, 1.0, 1.0, 1.0, 1.0, false);
         MagicManager.spawnParticles(serverLevel, ParticleHelper.SUNBEAM, spawnPosition.x, spawnPosition.y, spawnPosition.z, 1, 1.0, 1.0, 1.0, 1.0, true);
      }
   }

   private boolean isTeammate(LivingEntity caster, LivingEntity target) {
      return caster.isAlliedTo(target);
   }

   private boolean isTamedCreature(LivingEntity caster, LivingEntity target) {
      return !(target instanceof TamableAnimal tamedAnimal) ? false : tamedAnimal.isTame() && tamedAnimal.getOwner() == caster;
   }

   public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
      return true;
   }
}
