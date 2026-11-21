package com.gametechbc.gametechbcsspellbooks.effects.Assassin;

import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;

public class AssassinEffect extends MobEffect {
   public AssassinEffect() {
      super(MobEffectCategory.BENEFICIAL, 0);
      this.addAttributeModifier(Attributes.MOVEMENT_SPEED, java.util.UUID.nameUUIDFromBytes("buff".getBytes()).toString(), 0.05, Operation.MULTIPLY_BASE);
      this.addAttributeModifier(Attributes.ATTACK_DAMAGE, java.util.UUID.nameUUIDFromBytes("buff".getBytes()).toString(), 0.1, Operation.MULTIPLY_BASE);
   }

   public void applyEffectTick(LivingEntity entity, int amplifier) {
      entity.addEffect(new MobEffectInstance(MobEffectRegistry.TRUE_INVISIBILITY.get(), 10, 0, false, false, false));
      if (!entity.level().isClientSide && entity.tickCount % 30 == 0) {
         ServerLevel serverLevel = (ServerLevel)entity.level();
         serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
      }

      return;
   }

   public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
      return true;
   }
}
