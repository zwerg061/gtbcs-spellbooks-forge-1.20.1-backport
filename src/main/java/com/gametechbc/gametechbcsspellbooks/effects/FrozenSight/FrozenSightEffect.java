package com.gametechbc.gametechbcsspellbooks.effects.FrozenSight;

import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;


public class FrozenSightEffect extends MobEffect {
   public FrozenSightEffect() {
      super(MobEffectCategory.HARMFUL, 5926017);
      this.addAttributeModifier(Attributes.MOVEMENT_SPEED, java.util.UUID.nameUUIDFromBytes("debuff".getBytes()).toString(), -1.0, Operation.MULTIPLY_TOTAL);
   }

   public void applyEffectTick(LivingEntity entity, int amplifier) {
      return;
   }

   public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
      return true;
   }
}
