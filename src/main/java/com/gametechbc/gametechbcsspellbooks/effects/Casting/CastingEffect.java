package com.gametechbc.gametechbcsspellbooks.effects.Casting;

import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;


public class CastingEffect extends MobEffect {
   public CastingEffect() {
      super(MobEffectCategory.NEUTRAL, 0);
      this.addAttributeModifier(Attributes.MOVEMENT_SPEED, java.util.UUID.nameUUIDFromBytes("debuff".getBytes()).toString(), -0.2, Operation.MULTIPLY_TOTAL);
   }

   public void applyEffectTick(LivingEntity LivingEntityIn, int amplifier) {
      return;
   }

   public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
      return true;
   }
}
