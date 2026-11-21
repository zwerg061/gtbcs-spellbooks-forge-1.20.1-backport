package com.gametechbc.gametechbcsspellbooks.effects;

import com.gametechbc.gametechbcsspellbooks.init.GSEffects;
import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import java.util.Set;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;


public class AerialCollapseHelperEffect extends MobEffect {
   public AerialCollapseHelperEffect() {
      super(MobEffectCategory.NEUTRAL, 0);
   }

   public void applyEffectTick(LivingEntity entity, int amplifier) {
      MobEffectInstance effectInstance = entity.getEffect(GSEffects.AERIAL_COLLAPSE_HELPER.get());
      if (effectInstance != null) {
         int duration = effectInstance.getDuration();
         if (duration > 60) {
            entity.removeEffect(GSEffects.AERIAL_COLLAPSE_HELPER.get());
            entity.addEffect(new MobEffectInstance(GSEffects.AERIAL_COLLAPSE_HELPER.get(), 60, amplifier, false, true));
         }

         if (duration == 5 && !entity.level().isClientSide) {
            CameraShakeManager.addCameraShake(new CameraShakeData(20, entity.position(), 8.0F));
         }
      }

      return;
   }

   public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
      return true;
   }
}
