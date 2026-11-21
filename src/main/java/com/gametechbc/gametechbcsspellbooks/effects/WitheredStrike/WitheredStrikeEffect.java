package com.gametechbc.gametechbcsspellbooks.effects.WitheredStrike;

import com.gametechbc.gametechbcsspellbooks.init.GSSounds;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import java.util.Random;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class WitheredStrikeEffect extends MobEffect {
   private static final Random RANDOM = new Random();

   public WitheredStrikeEffect() {
      super(MobEffectCategory.BENEFICIAL, 0);
      this.addAttributeModifier(Attributes.ATTACK_DAMAGE, java.util.UUID.nameUUIDFromBytes("buff".getBytes()).toString(), 0.06, Operation.MULTIPLY_BASE);
   }

   public void applyEffectTick(LivingEntity entity, int amplifier) {
      super.applyEffectTick(entity, amplifier);
      return;
   }

   public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
      return true;
   }

   public void onAttack(LivingEntity attacker, LivingEntity target, int amplifier) {
      if (attacker instanceof Player player && RANDOM.nextFloat() < 0.5) {
         if (!this.isAlly(player, target)) {
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, amplifier));
            this.spawnShockWaveParticle(target);
            this.playSound(player, (SoundEvent)GSSounds.ABYSSAL_STRIKE_TRIGGER.get());
         }

         AABB areaOfEffect = player.getBoundingBox().inflate(8.0);
         player.level()
            .getEntitiesOfClass(LivingEntity.class, areaOfEffect, entity -> entity != player && !this.isAlly(player, entity))
            .forEach(entity -> entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, amplifier)));
      }
   }

   private void spawnShockWaveParticle(LivingEntity entity) {
      if (entity.level() instanceof ServerLevel serverLevel) {
         MagicManager.spawnParticles(
            serverLevel,
            new BlastwaveParticleOptions(((SchoolType)SchoolRegistry.HOLY.get()).getTargetingColor(), 8.0F),
            entity.getX(),
            entity.getY() + 0.165F,
            entity.getZ(),
            1,
            0.0,
            0.0,
            0.0,
            0.0,
            true
         );
      }
   }

   private void playSound(Player player, SoundEvent soundEvent) {
      if (soundEvent != null && !player.level().isClientSide()) {
         player.playNotifySound(soundEvent, SoundSource.PLAYERS, 1.0F, 1.0F);
      }
   }

   private boolean isAlly(Player player, LivingEntity entity) {
      if (entity instanceof Player) {
         return player.isAlliedTo((Player)entity);
      } else {
         return !(entity instanceof TamableAnimal tamable) ? false : tamable.isTame() && tamable.getOwner() == player;
      }
   }
}
