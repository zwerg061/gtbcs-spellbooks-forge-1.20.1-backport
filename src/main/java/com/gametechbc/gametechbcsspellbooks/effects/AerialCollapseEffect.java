package com.gametechbc.gametechbcsspellbooks.effects;

import com.gametechbc.gametechbcsspellbooks.init.GSEffects;
import com.gametechbc.gametechbcsspellbooks.util.GSDamageTypes;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Random;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class AerialCollapseEffect extends MobEffect {
   private static final Random RANDOM = new Random();

   public AerialCollapseEffect() {
      super(MobEffectCategory.HARMFUL, 0);
   }

   public void applyEffectTick(LivingEntity entity, int amplifier) {
      MobEffectInstance effectInstance = entity.getEffect(GSEffects.AERIAL_COLLAPSE.get());
      if (effectInstance != null) {
         int duration = effectInstance.getDuration();
         if (duration > 60) {
            entity.removeEffect(GSEffects.AERIAL_COLLAPSE.get());
            entity.addEffect(new MobEffectInstance(GSEffects.AERIAL_COLLAPSE.get(), 60, amplifier, false, true));
         }

         if (duration > 20) {
            entity.setDeltaMovement(entity.getDeltaMovement().x, 0.3, entity.getDeltaMovement().z);
         } else if (duration <= 10) {
            entity.setDeltaMovement(entity.getDeltaMovement().x, -3.0, entity.getDeltaMovement().z);
            if (duration == 5 && this.canApplyHealthReduction(entity)) {
               float maxHealth = entity.getMaxHealth();
               float damagePercent = 0.01F * (amplifier + 1);
               float damageAmount = maxHealth * damagePercent;
               Holder<DamageType> damageTypeHolder = entity.level()
                  .registryAccess()
                  .registryOrThrow(Registries.DAMAGE_TYPE)
                  .getHolderOrThrow(GSDamageTypes.AERIAL_COLLAPSE);
               DamageSource damageSource = new DamageSource(damageTypeHolder);
               entity.hurt(damageSource, damageAmount);
            }
         }

         if (!entity.level().isClientSide && entity.tickCount % 2 == 0) {
            for (int i = 0; i < 5; i++) {
               double offsetX = (RANDOM.nextDouble() - 0.5) * entity.getBbWidth();
               double offsetY = RANDOM.nextDouble() * entity.getBbHeight();
               double offsetZ = (RANDOM.nextDouble() - 0.5) * entity.getBbWidth();
               ((ServerLevel)entity.level())
                  .sendParticles(ParticleHelper.ACID_BUBBLE, entity.getX() + offsetX, entity.getY() + offsetY, entity.getZ() + offsetZ, 1, 0.0, 0.0, 0.0, 0.0);
            }
         }
      }

      return;
   }

   private boolean canApplyHealthReduction(LivingEntity entity) {
      return !(entity instanceof Player player) ? !entity.isInvulnerable() : !player.isCreative() && !player.isSpectator();
   }

   public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
      return true;
   }
}
