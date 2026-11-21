package com.gametechbc.gametechbcsspellbooks.effects;

import com.gametechbc.gametechbcsspellbooks.init.GSEffects;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.client.renderer.FogRenderer.FogData;
import net.minecraft.client.renderer.FogRenderer.FogMode;
import net.minecraft.client.renderer.FogRenderer.MobEffectFogFunction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class HemorrhageEffect extends MobEffect {
   private static final ResourceLocation HOLY_MAGIC_DOWN_ID = new ResourceLocation("gametechbcs_spellbooks", "hemmo_fire_magic_resist");
   private static final ResourceLocation MAGIC_UP_ID = new ResourceLocation("gametechbcs_spellbooks", "hemmo_magic_power");

   public HemorrhageEffect() {
      super(MobEffectCategory.NEUTRAL, 0);
      this.addAttributeModifier(AttributeRegistry.HOLY_MAGIC_RESIST.get(), java.util.UUID.nameUUIDFromBytes(HOLY_MAGIC_DOWN_ID.toString().getBytes()).toString(), -0.05, Operation.MULTIPLY_TOTAL);
      this.addAttributeModifier(AttributeRegistry.SPELL_POWER.get(), java.util.UUID.nameUUIDFromBytes(MAGIC_UP_ID.toString().getBytes()).toString(), 0.05, Operation.MULTIPLY_BASE);
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
               ParticleHelper.BLOOD, entity.getX() + offsetX, entity.getY() + offsetY, entity.getZ() + offsetZ, 1, velocityX, velocityY, velocityZ, 0.0
            );
         }
      }

      return;
   }

   public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
      return true;
   }

   @OnlyIn(Dist.CLIENT)
   public static class HemorrhageFogFunction implements MobEffectFogFunction {
      public HemorrhageFogFunction() {
      }

      public MobEffect getMobEffect() {
         return GSEffects.HEMORRHAGE.get();
      }

      public void setupFog(FogData fogData, LivingEntity entity, MobEffectInstance mobEffectInstance, float p_234184_, float p_234185_) {
         float f = 160.0F;
         if (fogData.mode == FogMode.FOG_SKY) {
            fogData.start = 0.0F;
            fogData.end = f * 0.25F;
         } else {
            fogData.start = -f * 0.5F;
            fogData.end = f;
         }
      }
   }
}
