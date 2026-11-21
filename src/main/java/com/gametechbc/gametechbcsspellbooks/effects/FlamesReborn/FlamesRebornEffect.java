package com.gametechbc.gametechbcsspellbooks.effects.FlamesReborn;

import com.gametechbc.gametechbcsspellbooks.init.GSEffects;
import com.gametechbc.gametechbcsspellbooks.init.GSSounds;
import com.gametechbc.spelllib.particle.ParticleDirection;
import com.gametechbc.spelllib.particle.SphereParticleManager;
import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector3f;

public class FlamesRebornEffect extends MagicMobEffect {
   private int tickCounter = 0;

   public FlamesRebornEffect() {
      super(MobEffectCategory.BENEFICIAL, 16729344);
   }

   public void applyEffectTick(LivingEntity entity, int amplifier) {
      if (!entity.level().isClientSide && entity.level() instanceof ServerLevel serverLevel) {
         int duration = entity.getEffect(GSEffects.FLAMES_REBORN.get()).getDuration();
         FlamesRebornHandler.RecordedPosition recordedPosition = FlamesRebornHandler.getRecordedPosition(entity.getUUID());
         if (recordedPosition != null) {
            MagicManager.spawnParticles(
               serverLevel,
               ParticleHelper.FIRE,
               recordedPosition.position.x,
               recordedPosition.position.y + 0.2,
               recordedPosition.position.z,
               10,
               0.2,
               1.5,
               0.2,
               0.095,
               false
            );
         }

         if (recordedPosition != null) {
            this.tickCounter++;
            int tickInterval;
            if (duration > 200) {
               tickInterval = 20;
            } else if (duration > 140) {
               tickInterval = 15;
            } else if (duration > 100) {
               tickInterval = 12;
            } else if (duration > 60) {
               tickInterval = 10;
            } else {
               tickInterval = 5;
            }

            if (this.tickCounter >= tickInterval) {
               this.tickCounter = 0;
               MagicManager.spawnParticles(
                  serverLevel,
                  new BlastwaveParticleOptions(new Vector3f(1.0F, 0.8F, 0.2F), 3.0F),
                  recordedPosition.position.x,
                  recordedPosition.position.y + 0.165F,
                  recordedPosition.position.z,
                  1,
                  0.0,
                  0.0,
                  0.0,
                  0.0,
                  true
               );
            }

            int particleCount = 1;

            for (int i = 0; i < particleCount; i++) {
               double offsetX = (entity.getRandom().nextDouble() - 0.5) * entity.getBbWidth();
               double offsetY = entity.getRandom().nextDouble() * entity.getBbHeight();
               double offsetZ = (entity.getRandom().nextDouble() - 0.5) * entity.getBbWidth();
               double velocityX = (entity.getRandom().nextDouble() - 0.5) * 0.2;
               double velocityY = (entity.getRandom().nextDouble() - 0.5) * 0.2;
               double velocityZ = (entity.getRandom().nextDouble() - 0.5) * 0.2;
               MagicManager.spawnParticles(
                  serverLevel,
                  ParticleHelper.FIRE,
                  entity.getX() + offsetX,
                  entity.getY() + offsetY,
                  entity.getZ() + offsetZ,
                  1,
                  velocityX,
                  velocityY,
                  velocityZ,
                  0.0,
                  false
               );
            }
         }
      }

      return;
   }

   public void onEffectAdded(LivingEntity entity, int amplifier) {
      super.onEffectAdded(entity, amplifier);
      FlamesRebornHandler.recordPosition(entity);
   }

   public void onEffectRemoved(LivingEntity entity, int amplifier) {
      super.onEffectRemoved(entity, amplifier);
      SphereParticleManager.spawnParticles(entity.level(), entity, 35, ParticleHelper.FIRE, ParticleDirection.INWARD, 3.0);
      FlamesRebornHandler.teleportEntityToRecordedPosition(entity);
      FlamesRebornHandler.RecordedPosition recordedPosition = FlamesRebornHandler.getRecordedPosition(entity.getUUID());
      if (recordedPosition != null) {
         SphereParticleManager.spawnParticles(entity.level(), entity, 50, ParticleHelper.FIRE, ParticleDirection.OUTWARD, 3.0);
         entity.level().playSound(null, entity, (SoundEvent)GSSounds.FLAMES_REBORN_REVIVE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
         if (!entity.level().isClientSide) {
            CameraShakeManager.addCameraShake(new CameraShakeData(15, entity.position(), 15.0F));
         }
      }

      if (entity instanceof Player player) {
         String[] messages = new String[]{
            "quote.gametechbcs_spellbooks.flames_reborn_revive_1",
            "quote.gametechbcs_spellbooks.flames_reborn_revive_2",
            "quote.gametechbcs_spellbooks.flames_reborn_revive_3",
            "quote.gametechbcs_spellbooks.flames_reborn_revive_4",
            "quote.gametechbcs_spellbooks.flames_reborn_revive_5",
            "quote.gametechbcs_spellbooks.flames_reborn_revive_6"
         };
         String selectedMessage = messages[player.getRandom().nextInt(messages.length)];
         player.displayClientMessage(Component.translatable(selectedMessage).withStyle(style -> style.withColor(16766783)), true);
      }

      FlamesRebornHandler.clearRecordedPosition(entity);
   }

   public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
      return true;
   }
}
