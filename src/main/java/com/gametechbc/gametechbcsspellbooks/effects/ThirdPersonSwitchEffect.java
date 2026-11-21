package com.gametechbc.gametechbcsspellbooks.effects;

import com.gametechbc.gametechbcsspellbooks.init.GSEffects;
import java.util.Set;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;


public class ThirdPersonSwitchEffect extends MobEffect {
   public ThirdPersonSwitchEffect() {
      super(MobEffectCategory.NEUTRAL, 0);
   }

   public void applyEffectTick(LivingEntity entity, int amplifier) {
      if (FMLEnvironment.dist == Dist.CLIENT && entity.level().isClientSide() && entity == Minecraft.getInstance().player) {
         int duration = entity.getEffect(GSEffects.THIRD_PERSON_SWITCH.get()).getDuration();
         if (duration > 2) {
            Minecraft.getInstance().options.setCameraType(CameraType.THIRD_PERSON_BACK);
         }

         if (duration <= 2) {
            Minecraft.getInstance().options.setCameraType(CameraType.FIRST_PERSON);
         }
      }

      super.applyEffectTick(entity, amplifier);
      return;
   }

   public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
      return true;
   }
}
