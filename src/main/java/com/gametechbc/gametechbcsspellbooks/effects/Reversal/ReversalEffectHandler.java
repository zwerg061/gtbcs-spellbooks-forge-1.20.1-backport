package com.gametechbc.gametechbcsspellbooks.effects.Reversal;

import com.gametechbc.gametechbcsspellbooks.init.GSEffects;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber(
   modid = "gametechbcs_spellbooks"
)
public class ReversalEffectHandler {
   private static final Map<UUID, Float> storedDamageMap = new HashMap<>();
   private static final Map<UUID, Projectile> lastProjectileHitMap = new HashMap<>();

   public ReversalEffectHandler() {
   }

   @SubscribeEvent
   public static void onLivingDamagePre(LivingDamageEvent event) {
      LivingEntity entity = event.getEntity();
      if (entity.hasEffect(GSEffects.REVERSAL.get())) {
         float damage = event.getAmount();
         storeDamage(entity, damage);
         if (event.getSource().getDirectEntity() instanceof Projectile projectile) {
            storeLastProjectileHit(entity, projectile);
            entity.removeEffect(GSEffects.REVERSAL.get());
         }

         entity.setDeltaMovement(0.0, 0.0, 0.0);
         entity.hurtMarked = false;
         entity.setNoActionTime(10);
         event.setAmount(0.0F);
      }
   }

   @SubscribeEvent
   public static void onLivingKnockBack(LivingKnockBackEvent event) {
      LivingEntity entity = event.getEntity();
      if (entity.hasEffect(GSEffects.REVERSAL.get())) {
         event.setCanceled(true);
      }
   }

   public static void storeDamage(LivingEntity entity, float damage) {
      storedDamageMap.put(entity.getUUID(), storedDamageMap.getOrDefault(entity.getUUID(), 0.0F) + damage);
   }

   public static float getStoredDamage(LivingEntity entity) {
      return storedDamageMap.getOrDefault(entity.getUUID(), 0.0F);
   }

   public static void clearStoredDamage(LivingEntity entity) {
      storedDamageMap.remove(entity.getUUID());
   }

   public static void storeLastProjectileHit(LivingEntity entity, Projectile projectile) {
      lastProjectileHitMap.put(entity.getUUID(), projectile);
   }

   public static Projectile getLastProjectileHit(LivingEntity entity) {
      return lastProjectileHitMap.get(entity.getUUID());
   }

   public static void clearLastProjectileHit(LivingEntity entity) {
      lastProjectileHitMap.remove(entity.getUUID());
   }
}
