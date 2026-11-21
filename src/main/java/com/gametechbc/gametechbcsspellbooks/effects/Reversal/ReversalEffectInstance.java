package com.gametechbc.gametechbcsspellbooks.effects.Reversal;

import com.gametechbc.gametechbcsspellbooks.init.GSEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.registries.RegistryObject;

public class ReversalEffectInstance extends MobEffectInstance {
   private float damageMultiplier;

   public ReversalEffectInstance(
      MobEffect effect,
      int duration,
      int amplifier,
      boolean ambient,
      boolean showParticles,
      boolean showIcon,
      float damageMultiplier
   ) {
      super(effect, duration, amplifier, ambient, showParticles, showIcon);
      this.damageMultiplier = damageMultiplier;
   }

   public float getDamageMultiplier() {
      return this.damageMultiplier;
   }

   public void setDamageMultiplier(float damageMultiplier) {
      this.damageMultiplier = damageMultiplier;
   }
}
