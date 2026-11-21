package com.gametechbc.gametechbcsspellbooks.effects.LingeringStrain;

import com.gametechbc.gametechbcsspellbooks.init.GSEffects;
import com.gametechbc.gametechbcsspellbooks.util.GSDamageTypes;
import io.redspace.ironsspellbooks.damage.DamageSources;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.TickEvent.ServerTickEvent;

@EventBusSubscriber(
   modid = "gametechbcs_spellbooks"
)
public class LingeringStrainHandler {
   private static final Map<UUID, LingeringStrainHandler.PendingDamage> pendingDamage = new HashMap<>();

   public LingeringStrainHandler() {
   }

   @SubscribeEvent
   public static void onLivingDamageEvent(LivingDamageEvent event) {
      LivingEntity entity = event.getEntity();
      DamageSource source = event.getSource();
      if (!source.is(GSDamageTypes.LINGERING_STRAIN)) {
         if (entity.hasEffect(GSEffects.LINGERING_STRAIN.get())) {
            float originalDamage = event.getAmount();
            event.setAmount(0.0F);
            float phasedDamage = originalDamage / 4.0F;
            pendingDamage.compute(entity.getUUID(), (uuid, existing) -> {
               if (existing == null) {
                  return new LingeringStrainHandler.PendingDamage(phasedDamage, 4, entity.level().dimension());
               } else {
                  existing.addDamage(phasedDamage);
                  return (LingeringStrainHandler.PendingDamage)existing;
               }
            });
         }
      }
   }

   @SubscribeEvent
   public static void onLivingDeathEvent(LivingDeathEvent event) {
      LivingEntity entity = event.getEntity();
      pendingDamage.remove(entity.getUUID());
   }

   @SubscribeEvent
   public static void onServerTick(ServerTickEvent event) {
      if (!pendingDamage.isEmpty()) {
         Iterator<Entry<UUID, LingeringStrainHandler.PendingDamage>> iterator = pendingDamage.entrySet().iterator();

         while (iterator.hasNext()) {
            Entry<UUID, LingeringStrainHandler.PendingDamage> entry = iterator.next();
            UUID entityUUID = entry.getKey();
            LingeringStrainHandler.PendingDamage damage = entry.getValue();
            ServerLevel entityLevel = event.getServer().getLevel(damage.dimension);
            if (entityLevel != null) {
               LivingEntity entity = (LivingEntity)entityLevel.getEntity(entityUUID);
               if (entity != null && entity.isAlive()) {
                  damage.tick(entity);
                  if (damage.isComplete()) {
                     iterator.remove();
                  }
               } else {
                  iterator.remove();
               }
            } else {
               iterator.remove();
            }
         }
      }
   }

   private static class PendingDamage {
      private float damagePerTick;
      private int ticksRemaining;
      private final ResourceKey<Level> dimension;
      private int tickCounter;

      public PendingDamage(float damagePerTick, int ticksRemaining, ResourceKey<Level> dimension) {
         this.damagePerTick = damagePerTick;
         this.ticksRemaining = ticksRemaining;
         this.dimension = dimension;
         this.tickCounter = 0;
      }

      public void addDamage(float extraDamage) {
         this.damagePerTick += extraDamage / 4.0F;
         this.ticksRemaining += 4;
      }

      public void tick(LivingEntity entity) {
         DamageSource damageSource = new DamageSource(DamageSources.getHolderFromResource(entity, GSDamageTypes.LINGERING_STRAIN), entity, null);
         if (this.ticksRemaining > 0) {
            this.tickCounter++;
            if (this.tickCounter >= 30) {
               int armorPieces = 0;

               for (ItemStack stack : entity.getArmorSlots()) {
                  if (!stack.isEmpty()) {
                     armorPieces++;
                  }
               }

               float armorMultiplier = 1.0F + armorPieces * 0.15F;
               float adjustedDamage = this.damagePerTick * armorMultiplier;
               DamageSources.ignoreNextKnockback(entity);
               entity.hurt(damageSource, adjustedDamage);
               this.ticksRemaining--;
               this.tickCounter = 0;
               entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.WARDEN_HEARTBEAT, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
         }
      }

      public boolean isComplete() {
         return this.ticksRemaining <= 0;
      }
   }
}
