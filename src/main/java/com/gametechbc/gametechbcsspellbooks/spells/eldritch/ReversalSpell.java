package com.gametechbc.gametechbcsspellbooks.spells.eldritch;

import com.gametechbc.gametechbcsspellbooks.api.spells.GSSpellAnimations;
import com.gametechbc.gametechbcsspellbooks.effects.Reversal.ReversalEffectInstance;
import com.gametechbc.gametechbcsspellbooks.init.GSEffects;
import com.gametechbc.gametechbcsspellbooks.init.GSSounds;
import com.gametechbc.gametechbcsspellbooks.util.GSTags;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@AutoSpellConfig
public class ReversalSpell extends AbstractSpell {
   private final ResourceLocation spellId = new ResourceLocation("gametechbcs_spellbooks", "reversal");
   private final DefaultConfig defaultConfig = new DefaultConfig()
      .setMinRarity(SpellRarity.RARE)
      .setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE)
      .setMaxLevel(3)
      .setCooldownSeconds(3.0)
      .build();

   public ReversalSpell() {
      this.manaCostPerLevel = 10;
      this.baseSpellPower = 1;
      this.spellPowerPerLevel = 1;
      this.castTime = 0;
      this.baseManaCost = 30;
   }

   public CastType getCastType() {
      return CastType.INSTANT;
   }

   public DefaultConfig getDefaultConfig() {
      return this.defaultConfig;
   }

   public ResourceLocation getSpellResource() {
      return this.spellId;
   }

   public Optional<SoundEvent> getCastStartSound() {
      return Optional.empty();
   }

   public Optional<SoundEvent> getCastFinishSound() {
      return Optional.of((SoundEvent)GSSounds.REVERSAL.get());
   }

   public AnimationHolder getCastFinishAnimation() {
      return GSSpellAnimations.REVERSAL;
   }

   public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
      if (entity.getMainHandItem().is(GSTags.CAN_CAST_REVERSAL)) {
         return true;
      } else {
         if (entity instanceof Player player && !level.isClientSide()) {
            player.displayClientMessage(Component.translatable("spell.gametechbcs_spellbooks.reversal.warning").withStyle(ChatFormatting.RED), true);
         }

         return false;
      }
   }

   public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
      float damageMultiplier = this.calculateDamageMultiplier(spellLevel, entity);
      ReversalEffectInstance reversalEffectInstance = new ReversalEffectInstance(GSEffects.REVERSAL.get(), 8, 0, true, false, false, damageMultiplier);
      entity.addEffect(reversalEffectInstance);
      MagicManager.spawnParticles(
         level,
         ParticleTypes.SCULK_SOUL,
         entity.getX(),
         entity.getY() + entity.getBbHeight() * 0.5F,
         entity.getZ(),
         30,
         entity.getBbWidth() * 0.5F,
         entity.getBbHeight() * 0.5F,
         entity.getBbWidth() * 0.5F,
         0.03,
         false
      );
      super.onCast(level, spellLevel, entity, castSource, playerMagicData);
   }

   private float calculateDamageMultiplier(int spellLevel, LivingEntity caster) {
      float spellPower = this.getSpellPower(spellLevel, caster);
      return 1.0F + 0.6F * spellPower;
   }

   public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
      float damageMultiplier = this.calculateDamageMultiplier(spellLevel, caster);
      return List.of(
         Component.translatable("ui.gametechbcs_spellbooks.reversal_damage_multiplier", new Object[]{String.format("%.1f", damageMultiplier)}),
         Component.literal("§9GTBC's Spellbooks")
      );
   }
}
