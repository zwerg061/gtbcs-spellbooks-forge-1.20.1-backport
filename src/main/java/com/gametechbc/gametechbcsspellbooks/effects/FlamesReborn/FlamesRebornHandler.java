package com.gametechbc.gametechbcsspellbooks.effects.FlamesReborn;

import com.gametechbc.gametechbcsspellbooks.init.GSEffects;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

@EventBusSubscriber(
   modid = "gametechbcs_spellbooks"
)
public class FlamesRebornHandler {
   private static final Map<UUID, FlamesRebornHandler.RecordedPosition> recordedPositions = new HashMap<>();
   private static final Map<UUID, Integer> initialDurations = new HashMap<>();

   public FlamesRebornHandler() {
   }

   @SubscribeEvent
   public static void onEntityDeath(LivingDeathEvent event) {
      LivingEntity entity = event.getEntity();
      if (entity.hasEffect(GSEffects.FLAMES_REBORN.get())) {
         event.setCanceled(true);
         entity.removeEffect(GSEffects.FLAMES_REBORN.get());
         clearRecordedPosition(entity);
      }
   }

   public static void recordPosition(LivingEntity entity) {
      if (!entity.level().isClientSide) {
         Vec3 position = entity.position();
         ResourceKey<Level> dimension = entity.level().dimension();
         Vec3 lookDirection = entity.getLookAngle();
         float health = entity.getHealth();
         recordedPositions.put(entity.getUUID(), new FlamesRebornHandler.RecordedPosition(position, dimension, lookDirection, health));
      }
   }

   public static void teleportEntityToRecordedPosition(LivingEntity entity) {
      FlamesRebornHandler.RecordedPosition recordedPosition = recordedPositions.get(entity.getUUID());
      if (recordedPosition != null && entity.level() instanceof ServerLevel currentLevel) {
         ResourceKey<Level> targetDimension = recordedPosition.dimension;
         Vec3 targetPosition = recordedPosition.position;
         if (!currentLevel.dimension().equals(targetDimension)) {
            ServerLevel targetLevel = currentLevel.getServer().getLevel(targetDimension);
            if (targetLevel == null) {
               System.err.println("Target dimension not found: " + targetDimension.location());
               return;
            }

            entity.teleportTo(targetLevel, targetPosition.x, targetPosition.y, targetPosition.z, java.util.Collections.emptySet(), entity.getYRot(), entity.getXRot());
         }

         entity.teleportTo(targetPosition.x, targetPosition.y, targetPosition.z);
         entity.setYHeadRot((float)recordedPosition.lookDirection.y);
         entity.setHealth(recordedPosition.health);
      }
   }

   public static void clearRecordedPosition(LivingEntity entity) {
      recordedPositions.remove(entity.getUUID());
      initialDurations.remove(entity.getUUID());
   }

   public static FlamesRebornHandler.RecordedPosition getRecordedPosition(UUID entityId) {
      return recordedPositions.get(entityId);
   }

   public static class RecordedPosition {
      final Vec3 position;
      final ResourceKey<Level> dimension;
      final Vec3 lookDirection;
      final float health;

      RecordedPosition(Vec3 position, ResourceKey<Level> dimension, Vec3 lookDirection, float health) {
         this.position = position;
         this.dimension = dimension;
         this.lookDirection = lookDirection;
         this.health = health;
      }
   }
}
