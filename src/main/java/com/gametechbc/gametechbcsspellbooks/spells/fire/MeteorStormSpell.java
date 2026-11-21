package com.gametechbc.gametechbcsspellbooks.spells.fire;

import com.gametechbc.gametechbcsspellbooks.api.spells.GSSpellAnimations;
import com.gametechbc.gametechbcsspellbooks.init.GSEffects;
import com.gametechbc.spelllib.particle.ParticleDirection;
import com.gametechbc.spelllib.particle.SphereParticleManager;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

@AutoSpellConfig
public class MeteorStormSpell extends AbstractSpell {
   private final ResourceLocation spellId = new ResourceLocation("gametechbcs_spellbooks", "meteor_storm");
   private final DefaultConfig defaultConfig = new DefaultConfig()
      .setMinRarity(SpellRarity.UNCOMMON)
      .setSchoolResource(SchoolRegistry.FIRE_RESOURCE)
      .setMaxLevel(6)
      .setCooldownSeconds(90.0)
      .build();

   public MeteorStormSpell() {
      this.manaCostPerLevel = 30;
      this.baseSpellPower = 1;
      this.spellPowerPerLevel = 1;
      this.castTime = 15;
      this.baseManaCost = 50;
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
      return Optional.of((SoundEvent)SoundRegistry.FIRE_CAST.get());
   }

   public Optional<SoundEvent> getCastFinishSound() {
      return Optional.of(SoundEvents.DRAGON_FIREBALL_EXPLODE);
   }

   public AnimationHolder getCastStartAnimation() {
      return GSSpellAnimations.METEOR_STORM_CAST;
   }

   public AnimationHolder getCastFinishAnimation() {
      return AnimationHolder.pass();
   }

   public int getEffectiveCastTime(int spellLevel, @Nullable LivingEntity entity) {
      return this.getCastTime(spellLevel);
   }

   public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
      int duration = this.getDuration(spellLevel);
      int damage = this.getDamage(entity, spellLevel);
      if (!entity.hasEffect(GSEffects.METEOR_STORM.get())) {
         entity.addEffect(new MobEffectInstance(GSEffects.METEOR_STORM.get(), duration, damage, false, true));
      }

      MagicManager.spawnParticles(
         level,
         new BlastwaveParticleOptions(((SchoolType)SchoolRegistry.FIRE.get()).getTargetingColor(), 1.0F),
         entity.getX(),
         entity.getY() + 0.165F,
         entity.getZ(),
         1,
         0.0,
         0.0,
         0.0,
         0.0,
         true
      );
      SphereParticleManager.spawnParticles(level, entity, 60, ParticleHelper.FIRE, ParticleDirection.OUTWARD, 3.5);
      CameraShakeManager.addCameraShake(new CameraShakeData(20, entity.position(), 26.0F));
      super.onCast(level, spellLevel, entity, castSource, playerMagicData);
   }

   public int getDuration(int spellLevel) {
      return 160 + spellLevel * 60;
   }

   public int getDamage(LivingEntity caster, int spellLevel) {
      return (int)(3.0 + this.getSpellPower(spellLevel, caster) * 2.0F);
   }

   public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
      int damage = this.getDamage(caster, spellLevel);
      int duration = this.getDuration(spellLevel);
      return List.of(
         Component.translatable("ui.gametechbcs_spellbooks.damage", new Object[]{Utils.stringTruncation(damage, 1)}),
         Component.translatable("ui.gametechbcs_spellbooks.radius", new Object[]{Utils.stringTruncation(24.0, 2)}),
         Component.translatable("ui.gametechbcs_spellbooks.meteor_storm_duration", new Object[]{Utils.timeFromTicks(duration, 2)}),
         Component.literal("§9GTBC's Spellbooks")
      );
   }
}
