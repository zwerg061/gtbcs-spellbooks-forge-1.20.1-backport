package com.gametechbc.gametechbcsspellbooks.effects.Assassin;

import com.gametechbc.gametechbcsspellbooks.effects.WitheredStrike.WitheredStrikeEffect;
import com.gametechbc.gametechbcsspellbooks.init.GSEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber
public class AttackHandler {
   public AttackHandler() {
   }

   @SubscribeEvent
   public static void onLivingDamageEvent(LivingDamageEvent event) {
      if (event.getSource().getEntity() instanceof LivingEntity attacker && attacker.hasEffect(GSEffects.ASSASSIN.get())) {
         attacker.removeEffect(GSEffects.ASSASSIN.get());
      }
   }

   @SubscribeEvent
   public static void onLivingDamage(LivingDamageEvent event) {
      if (event.getSource().getEntity() instanceof LivingEntity attacker && attacker.hasEffect(GSEffects.WITHERED_STRIKE.get())) {
         int amplifier = attacker.getEffect(GSEffects.WITHERED_STRIKE.get()).getAmplifier();
         ((WitheredStrikeEffect)GSEffects.WITHERED_STRIKE.get()).onAttack(attacker, event.getEntity(), amplifier);
      }
   }
}
