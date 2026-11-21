package com.gametechbc.gametechbcsspellbooks.effects;

import com.gametechbc.gametechbcsspellbooks.init.GSEffects;
import com.gametechbc.gametechbcsspellbooks.init.GSItems;
import com.gametechbc.gametechbcsspellbooks.init.GSSounds;
import com.gametechbc.gametechbcsspellbooks.util.GSTags;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import top.theillusivec4.curios.api.CuriosApi;

public class SpectralBlinkEffect extends MobEffect {
   public SpectralBlinkEffect() {
      super(MobEffectCategory.BENEFICIAL, 16777215);
   }

   public void applyEffectTick(LivingEntity entity, int amplifier) {
      if (entity != null) {
         Level world = entity.level();
         MobEffectInstance effectInstance = entity.getEffect(GSEffects.SPECTRAL_BLINK.get());
         if (effectInstance != null) {
            int duration = effectInstance.getDuration();
            int level = amplifier + 1;
            double range = 1.0 + level;
            if (duration > 60) {
               entity.removeEffect(GSEffects.SPECTRAL_BLINK.get());
               entity.addEffect(new MobEffectInstance(GSEffects.SPECTRAL_BLINK.get(), 60, amplifier, false, true));
            }

            if (duration == 60) {
               world.playSound(null, entity.blockPosition(), (SoundEvent)GSSounds.SPECTRAL_BLINK_CHARGE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }

            for (Entity nearbyEntity : world.getEntities(entity, entity.getBoundingBox().inflate(range))) {
               if (nearbyEntity instanceof LivingEntity livingEntity && nearbyEntity != entity) {
                  livingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 10, 0, false, false));
               }
            }

            if (duration == 1) {
               this.handleBlinkLogic(entity, amplifier, range, world);
            }
         }
      }

      return;
   }

   private void handleBlinkLogic(LivingEntity entity, int amplifier, double range, Level world) {
      boolean hasSpectralShift = CuriosApi.getCuriosHelper().findEquippedCurio((Item)GSItems.AMULET_OF_SPECTRAL_SHIFT.get(), entity).isPresent();
      if (entity.isCrouching() && hasSpectralShift) {
         this.teleportEntityToPlayer(entity, range, world);
      } else {
         this.teleportPlayerToEntity(entity, range, world);
      }
   }

   private void teleportEntityToPlayer(LivingEntity entity, double range, Level world) {
      Vec3 startVec = entity.getEyePosition(1.0F);
      Vec3 viewVec = entity.getLookAngle();
      Vec3 endVec = startVec.add(viewVec.scale(range));
      AABB boundingBox = entity.getBoundingBox().expandTowards(viewVec.scale(range)).inflate(1.0);
      EntityHitResult entityHitResult = this.getForgivingEntityHitResult(world, entity, startVec, endVec, boundingBox);
      if (entityHitResult != null && entityHitResult.getType() == Type.ENTITY) {
         Entity hitEntity = entityHitResult.getEntity();
         if (hitEntity.getType().is(GSTags.SPECTRAL_SHIFT_BLACKLIST)) {
            if (entity instanceof Player player) {
               player.displayClientMessage(Component.literal(hitEntity.getName().getString() + " cannot be teleported!"), true);
            }

            return;
         }

         Vec3 targetPos = entity.position();
         hitEntity.teleportTo(targetPos.x, targetPos.y, targetPos.z);
         world.playSound(null, entity.blockPosition(), (SoundEvent)GSSounds.SPECTRAL_BLINK_SUCCESS.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
         if (entity instanceof Player player) {
            player.displayClientMessage(Component.literal("Teleported " + hitEntity.getName().getString() + " to you!"), true);
         }
      } else {
         this.playFailedBlinkPostEffects(world, entity);
      }
   }

   private void teleportPlayerToEntity(LivingEntity entity, double range, Level world) {
      Vec3 startVec = entity.getEyePosition(1.0F);
      Vec3 viewVec = entity.getLookAngle();
      Vec3 endVec = startVec.add(viewVec.scale(range));
      AABB boundingBox = entity.getBoundingBox().expandTowards(viewVec.scale(range)).inflate(1.0);
      EntityHitResult entityHitResult = this.getForgivingEntityHitResult(world, entity, startVec, endVec, boundingBox);
      if (entityHitResult != null && entityHitResult.getType() == Type.ENTITY) {
         Entity hitEntity = entityHitResult.getEntity();
         if (hitEntity.getType().is(GSTags.SPECTRAL_BLINK_BLACKLIST)) {
            if (entity instanceof Player player) {
               player.displayClientMessage(Component.literal(hitEntity.getName().getString() + " cannot be teleported to!"), true);
            }

            return;
         }

         Vec3 targetPos = hitEntity.position();
         entity.teleportTo(targetPos.x, targetPos.y, targetPos.z);
         world.playSound(null, entity.blockPosition(), (SoundEvent)GSSounds.SPECTRAL_BLINK_SUCCESS.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
         if (entity instanceof Player player) {
            player.displayClientMessage(Component.literal("Spectral Blinked to " + hitEntity.getName().getString()), true);
         }
      } else {
         this.playFailedBlinkPostEffects(world, entity);
      }
   }

   private void playFailedBlinkPostEffects(Level world, Entity entity) {
      world.playSound(null, entity.blockPosition(), (SoundEvent)GSSounds.SPECTRAL_BLINK_FAILED.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
      if (entity instanceof Player player) {
         player.displayClientMessage(Component.literal("Spectral Blink failed: No entities detected in the range"), true);
      }
   }

   public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
      return true;
   }

   private EntityHitResult getForgivingEntityHitResult(Level world, LivingEntity entity, Vec3 startVec, Vec3 endVec, AABB boundingBox) {
      EntityHitResult entityHitResult = null;
      double closestDistance = Double.MAX_VALUE;
      double forgivenessRadius = 0.5;

      for (Entity targetEntity : world.getEntities(entity, boundingBox, e -> !e.isSpectator() && e.isPickable())) {
         AABB entityBoundingBox = targetEntity.getBoundingBox().inflate(targetEntity.getPickRadius() + forgivenessRadius);
         if (entityBoundingBox.clip(startVec, endVec).isPresent()) {
            double distance = startVec.distanceTo(targetEntity.getBoundingBox().getCenter());
            if (distance < closestDistance) {
               closestDistance = distance;
               entityHitResult = new EntityHitResult(targetEntity);
            }
         }
      }

      return entityHitResult;
   }
}
