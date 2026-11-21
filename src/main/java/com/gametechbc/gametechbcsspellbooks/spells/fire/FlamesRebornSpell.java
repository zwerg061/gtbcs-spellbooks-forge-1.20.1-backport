package com.gametechbc.gametechbcsspellbooks.spells.fire;

import com.gametechbc.gametechbcsspellbooks.api.spells.GSSpellAnimations;
import com.gametechbc.gametechbcsspellbooks.init.GSEffects;
import com.gametechbc.gametechbcsspellbooks.init.GSSounds;
import com.gametechbc.spelllib.particle.ParticleDirection;
import com.gametechbc.spelllib.particle.SphereParticleManager;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

@AutoSpellConfig
public class FlamesRebornSpell extends AbstractSpell {
   private final ResourceLocation spellId = new ResourceLocation("gametechbcs_spellbooks", "flames_reborn");
   private final DefaultConfig defaultConfig = new DefaultConfig()
      .setMinRarity(SpellRarity.RARE)
      .setSchoolResource(SchoolRegistry.FIRE_RESOURCE)
      .setMaxLevel(3)
      .setCooldownSeconds(120.0)
      .build();

   public FlamesRebornSpell() {
      this.baseManaCost = 50;
      this.manaCostPerLevel = 75;
      this.baseSpellPower = 1;
      this.spellPowerPerLevel = 1;
      this.castTime = 10;
   }

   public CastType getCastType() {
      return CastType.LONG;
   }

   public DefaultConfig getDefaultConfig() {
      return this.defaultConfig;
   }

   public ResourceLocation getSpellResource() {
      return this.spellId;
   }

   public Optional<SoundEvent> getCastStartSound() {
      return Optional.of((SoundEvent)GSSounds.FLAMES_REBORN_REAPPEAR.get());
   }

   public Optional<SoundEvent> getCastFinishSound() {
      return Optional.empty();
   }

   public AnimationHolder getCastStartAnimation() {
      return GSSpellAnimations.METEOR_STORM_CAST;
   }

   public AnimationHolder getCastFinishAnimation() {
      return AnimationHolder.pass();
   }

   public void onServerCastTick(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
      SphereParticleManager.spawnParticles(level, entity, 6, ParticleHelper.FIRE, ParticleDirection.INWARD, 2.0);
      super.onServerCastTick(level, spellLevel, entity, playerMagicData);
   }

   public void onServerPreCast(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
      CameraShakeManager.addCameraShake(new CameraShakeData(20, entity.position(), 15.0F));
      super.onServerPreCast(level, spellLevel, entity, playerMagicData);
   }

   public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
      int duration = this.getDuration(entity, spellLevel);
      entity.addEffect(new MobEffectInstance(GSEffects.FLAMES_REBORN.get(), duration, 0, false, false, true));
      super.onCast(level, spellLevel, entity, castSource, playerMagicData);
   }

   public int getDuration(LivingEntity caster, int spellLevel) {
      return 230 + (int)this.getSpellPower(spellLevel, caster) * 90;
   }

   public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
      return List.of(
         Component.translatable("ui.gametechbcs_spellbooks.effect_length", new Object[]{Utils.timeFromTicks(this.getDuration(caster, spellLevel), 1)}),
         Component.literal("§9GTBC's Spellbooks")
      );
   }
}
