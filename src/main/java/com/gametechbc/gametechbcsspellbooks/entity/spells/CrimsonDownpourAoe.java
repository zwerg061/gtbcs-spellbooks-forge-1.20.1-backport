package com.gametechbc.gametechbcsspellbooks.entity.spells;

import com.gametechbc.gametechbcsspellbooks.init.GSEffects;
import com.gametechbc.gametechbcsspellbooks.init.GSEntities;
import com.gametechbc.gametechbcsspellbooks.init.GSSounds;
import com.gametechbc.gametechbcsspellbooks.init.GSSpells;
import com.gametechbc.gametechbcsspellbooks.util.GSParticleHelper;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Optional;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class CrimsonDownpourAoe extends AoeEntity implements AntiMagicSusceptible {
   private int hemmoEffectAmplifier = 0;
   private int tickCounter;
   private float lifestealAmount = 0.2F;

   public CrimsonDownpourAoe(EntityType<? extends Projectile> pEntityType, Level pLevel) {
      super(pEntityType, pLevel);
   }

   public CrimsonDownpourAoe(Level level) {
      this((EntityType<? extends Projectile>)GSEntities.BLOOD_RAIN_AOE.get(), level);
   }

   public EntityDimensions getDimensions(Pose pPose) {
      return EntityDimensions.scalable(this.getRadius() * 2.0F, this.getRadius() * 1.2F);
   }

   protected boolean canHitTargetForGroundContext(LivingEntity target) {
      return true;
   }

   public int getHemorrhageEffectAmplifier() {
      return this.hemmoEffectAmplifier;
   }

   public void setHemorrhageEffectAmplifier(int amplifier) {
      this.hemmoEffectAmplifier = amplifier;
   }

   public float getLifestealAmount() {
      return this.lifestealAmount;
   }

   public void setLifestealAmount(float amount) {
      this.lifestealAmount = amount;
   }

   public void tick() {
      super.tick();
      this.tickCounter++;
      if (!this.level().isClientSide && this.tickCounter == 1) {
         this.playSound((SoundEvent)GSSounds.ACID_RAIN_ACTIVE.get(), 1.5F, 1.0F);
      }

      if (this.tickCount % 20 == 0) {
         this.applyLifestealDamage();
      }

      this.spawnBloodParticles();
      if (this.tickCount % 5 == 0) {
         this.applyHemorrhageEffect();
         this.spawnCloudParticles();
      }

      if (!this.level().isClientSide && this.tickCount >= 140) {
         this.discard();
      }
   }

   private void applyHemorrhageEffect() {
      this.level().getEntities(this, this.getBoundingBox(), entity -> entity instanceof LivingEntity).forEach(entity -> {
         LivingEntity target = (LivingEntity)entity;
         target.addEffect(new MobEffectInstance(GSEffects.HEMORRHAGE.get(), 100, this.getHemorrhageEffectAmplifier(), false, false, true));
      });
   }

   private void applyLifestealDamage() {
      LivingEntity owner = (LivingEntity)this.getOwner();
      if (owner != null) {
         this.level().getEntities(this, this.getBoundingBox(), entity -> entity instanceof LivingEntity).forEach(entity -> {
            LivingEntity target = (LivingEntity)entity;
            if (target != owner && !this.isAlly(owner, target) && !this.isTamed(target)) {
               float damage = this.getDamage();
               SpellDamageSource damageSource = GSSpells.CRIMSON_DOWNPOUR_SPELL.get().getDamageSource(this, owner);
               DamageSources.ignoreNextKnockback(target);
               if (target.hurt(damageSource, damage)) {
                  owner.heal(damage * this.lifestealAmount);
               }
            }
         });
      }
   }

   private boolean isAlly(LivingEntity owner, LivingEntity target) {
      return owner.getTeam() != null && owner.getTeam().isAlliedTo(target.getTeam());
   }

   private boolean isTamed(LivingEntity target) {
      return target instanceof TamableAnimal tamableAnimal ? tamableAnimal.isTame() : false;
   }

   private void spawnBloodParticles() {
      double radius = this.getRadius();
      int particleCount = (int)(6.0F * this.getRadius());

      for (int i = 0; i < particleCount; i++) {
         double randomX = this.getX() + (this.random.nextDouble() * 2.0 * radius - radius);
         double randomY = this.getY() + 12.0;
         double randomZ = this.getZ() + (this.random.nextDouble() * 2.0 * radius - radius);
         double velocityY = -0.5 - this.random.nextDouble() * 0.5;
         this.level().addParticle(ParticleHelper.BLOOD, randomX, randomY, randomZ, 0.0, velocityY, 0.0);
      }
   }

   private void spawnCloudParticles() {
      double radius = this.getRadius();
      int particleCount = (int)(1.0F * this.getRadius());

      for (int i = 0; i < particleCount; i++) {
         double randomX = this.getX() + (this.random.nextDouble() * 2.0 * radius - radius);
         double randomY = this.getY() + 14.0;
         double randomZ = this.getZ() + (this.random.nextDouble() * 2.0 * radius - radius);
         double velocityY = -0.5 - this.random.nextDouble() * 0.5;
         this.level().addParticle(GSParticleHelper.CRIMSON_CLOUD, randomX, randomY, randomZ, 0.0, velocityY, 0.0);
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
      compound.putInt("HemorrhageEffectAmplifier", this.hemmoEffectAmplifier);
      compound.putFloat("LifestealAmount", this.lifestealAmount);
   }

   protected void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      if (compound.contains("HemorrhageEffectAmplifier", 3)) {
         this.hemmoEffectAmplifier = compound.getInt("HemorrhageEffectAmplifier");
      }

      if (compound.contains("LifestealAmount", 5)) {
         this.lifestealAmount = compound.getFloat("LifestealAmount");
      }
   }
}
