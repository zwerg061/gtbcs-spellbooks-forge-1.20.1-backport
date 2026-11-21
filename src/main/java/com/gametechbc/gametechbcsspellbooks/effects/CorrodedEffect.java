package com.gametechbc.gametechbcsspellbooks.effects;

import com.gametechbc.gametechbcsspellbooks.init.GSParticles;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;

public class CorrodedEffect extends MobEffect {
   private static final ResourceLocation ARMOR_DOWN_ID = new ResourceLocation("gametechbcs_spellbooks", "corroded_armor");
   private static final ResourceLocation FIRE_MAGIC_DOWN_ID = new ResourceLocation("gametechbcs_spellbooks", "corroded_fire_magic_resist");

   public CorrodedEffect() {
      super(MobEffectCategory.HARMFUL, 0);
      this.addAttributeModifier(AttributeRegistry.FIRE_MAGIC_RESIST.get(), java.util.UUID.nameUUIDFromBytes(FIRE_MAGIC_DOWN_ID.toString().getBytes()).toString(), -0.05, Operation.MULTIPLY_TOTAL);
      this.addAttributeModifier(Attributes.ARMOR, java.util.UUID.nameUUIDFromBytes(ARMOR_DOWN_ID.toString().getBytes()).toString(), -0.025, Operation.MULTIPLY_TOTAL);
   }

   public void applyEffectTick(LivingEntity entity, int amplifier) {
      if (!entity.level().isClientSide() && entity.level() instanceof ServerLevel serverLevel) {
         int particleCount = 1;

         for (int i = 0; i < particleCount; i++) {
            double offsetX = (entity.getRandom().nextDouble() - 0.5) * entity.getBbWidth();
            double offsetY = entity.getRandom().nextDouble() * entity.getBbHeight();
            double offsetZ = (entity.getRandom().nextDouble() - 0.5) * entity.getBbWidth();
            double velocityX = (entity.getRandom().nextDouble() - 0.5) * 0.2;
            double velocityY = (entity.getRandom().nextDouble() - 0.5) * 0.2;
            double velocityZ = (entity.getRandom().nextDouble() - 0.5) * 0.2;
            serverLevel.sendParticles(
               GSParticles.ACID_DROP_PARTICLE.get(),
               entity.getX() + offsetX,
               entity.getY() + offsetY,
               entity.getZ() + offsetZ,
               1,
               velocityX,
               velocityY,
               velocityZ,
               0.0
            );
         }
      }

      return;
   }

   public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
      return true;
   }
}
