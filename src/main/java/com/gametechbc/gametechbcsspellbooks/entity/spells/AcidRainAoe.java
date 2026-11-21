package com.gametechbc.gametechbcsspellbooks.entity.spells;

import com.gametechbc.gametechbcsspellbooks.init.GSEffects;
import com.gametechbc.gametechbcsspellbooks.init.GSEntities;
import com.gametechbc.gametechbcsspellbooks.init.GSParticles;
import com.gametechbc.gametechbcsspellbooks.init.GSSounds;
import com.gametechbc.gametechbcsspellbooks.util.GSTags;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class AcidRainAoe extends AoeEntity implements AntiMagicSusceptible {
   private int corrodedEffectAmplifier = 0;
   private int tickCounter;

   public AcidRainAoe(EntityType<? extends Projectile> pEntityType, Level pLevel) {
      super(pEntityType, pLevel);
   }

   public AcidRainAoe(Level level) {
      this((EntityType<? extends Projectile>)GSEntities.ACID_RAIN_AOE.get(), level);
   }

   public int getCorrodedEffectAmplifier() {
      return this.corrodedEffectAmplifier;
   }

   public void setCorrodedEffectAmplifier(int corrodedEffectAmplifier) {
      this.corrodedEffectAmplifier = corrodedEffectAmplifier;
   }

   public void tick() {
      super.tick();
      this.tickCounter++;
      if (!this.level().isClientSide && this.tickCounter == 1) {
         this.playSound((SoundEvent)GSSounds.ACID_RAIN_ACTIVE.get(), 1.5F, 1.0F);
      }

      if (this.tickCount % 5 == 0) {
         this.cleanseEntities();
         this.applyCorrodedEffectToEntities();
         this.increaseFireTicksForEntities();
      }

      this.spawnAcidParticles();
      if (this.tickCount % 5 == 0) {
         this.spawnCloudParticles();
      }

      if (!this.level().isClientSide && this.tickCount >= 140) {
         this.discard();
      }
   }

   private void applyCorrodedEffectToEntities() {
      this.level().getEntities(this, this.getBoundingBox(), entity -> entity instanceof LivingEntity).forEach(entity -> {
         LivingEntity target = (LivingEntity)entity;
         target.addEffect(new MobEffectInstance(GSEffects.CORRODED.get(), 100, this.getCorrodedEffectAmplifier(), false, false, true));
      });
   }

   private void increaseFireTicksForEntities() {
      this.level().getEntities(this, this.getBoundingBox(), entity -> entity instanceof LivingEntity).forEach(entity -> {
         if (entity.isOnFire()) {
            entity.setSecondsOnFire((int)Math.ceil(entity.getRemainingFireTicks() / 20.0F + 1.0F));
         }
      });
   }

   private void cleanseEntities() {
      LivingEntity owner = (LivingEntity)this.getOwner();
      if (owner != null) {
         this.level().getEntities(this, this.getBoundingBox(), entity -> entity instanceof LivingEntity).forEach(entity -> {
            LivingEntity target = (LivingEntity)entity;
            if (target != owner && !this.isAlly(owner, target) && !this.isTamed(target)) {
               this.cleanseBeneficialEffects(target);
            }
         });
      }
   }

   private void cleanseBeneficialEffects(LivingEntity entity) {
      List<MobEffect> beneficialEffects = entity.getActiveEffects()
         .stream()
         .map(MobEffectInstance::getEffect)
         .filter(effect -> effect.getCategory() == MobEffectCategory.BENEFICIAL)
         .filter(holder -> !net.minecraft.core.registries.BuiltInRegistries.MOB_EFFECT.wrapAsHolder(holder).is(GSTags.BANISH_IMMUNE))
         .toList();
      beneficialEffects.forEach(entity::removeEffect);
   }

   private boolean isAlly(LivingEntity owner, LivingEntity target) {
      return owner.getTeam() != null && owner.getTeam().isAlliedTo(target.getTeam());
   }

   private boolean isTamed(LivingEntity target) {
      return target instanceof TamableAnimal tamableAnimal ? tamableAnimal.isTame() : false;
   }

   private void spawnAcidParticles() {
      double radius = this.getRadius();
      int particleCount = (int)(6.0F * this.getRadius());

      for (int i = 0; i < particleCount; i++) {
         double randomX = this.getX() + (this.random.nextDouble() * 2.0 * radius - radius);
         double randomY = this.getY() + 10.0;
         double randomZ = this.getZ() + (this.random.nextDouble() * 2.0 * radius - radius);
         double velocityY = -0.5 - this.random.nextDouble() * 0.5;
         this.level().addParticle((ParticleOptions)GSParticles.ACID_DROP_PARTICLE.get(), randomX, randomY, randomZ, 0.0, velocityY, 0.0);
      }
   }

   private void spawnCloudParticles() {
      double radius = this.getRadius();
      int particleCount = (int)(1.0F * this.getRadius());

      for (int i = 0; i < particleCount; i++) {
         double randomX = this.getX() + (this.random.nextDouble() * 2.0 * radius - radius);
         double randomY = this.getY() + 12.0;
         double randomZ = this.getZ() + (this.random.nextDouble() * 2.0 * radius - radius);
         double velocityY = -0.5 - this.random.nextDouble() * 0.5;
         this.level().addParticle(ParticleHelper.POISON_CLOUD, randomX, randomY, randomZ, 0.0, velocityY, 0.0);
      }
   }

   public void applyEffect(LivingEntity target) {
   }

   public float getParticleCount() {
      return 0.0F;
   }

   protected float particleYOffset() {
      return 0.25F;
   }

   protected float getParticleSpeedModifier() {
      return 1.4F;
   }

   public Optional<ParticleOptions> getParticle() {
      return Optional.empty();
   }

   public void onAntiMagic(MagicData magicData) {
      this.discard();
   }

   protected void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putInt("CorrodedEffectAmplifier", this.corrodedEffectAmplifier);
   }

   protected void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      if (compound.contains("CorrodedEffectAmplifier", 3)) {
         this.corrodedEffectAmplifier = compound.getInt("CorrodedEffectAmplifier");
      }
   }
}
