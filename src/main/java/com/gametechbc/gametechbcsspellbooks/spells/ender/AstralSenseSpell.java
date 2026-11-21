package com.gametechbc.gametechbcsspellbooks.spells.ender;

import com.gametechbc.gametechbcsspellbooks.init.GSEffects;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

@AutoSpellConfig
public class AstralSenseSpell extends AbstractSpell {
   private final ResourceLocation spellId = new ResourceLocation("gametechbcs_spellbooks", "astral_sense");
   private final DefaultConfig defaultConfig = new DefaultConfig()
      .setMinRarity(SpellRarity.RARE)
      .setSchoolResource(SchoolRegistry.ENDER_RESOURCE)
      .setMaxLevel(3)
      .setCooldownSeconds(120.0)
      .build();

   public AstralSenseSpell() {
      this.manaCostPerLevel = 50;
      this.baseSpellPower = 1;
      this.spellPowerPerLevel = 1;
      this.castTime = 0;
      this.baseManaCost = 100;
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
      return Optional.of((SoundEvent)SoundRegistry.BLACK_HOLE_CAST.get());
   }

   public AnimationHolder getCastFinishAnimation() {
      return SpellAnimations.SLASH_ANIMATION;
   }

   public void onCast(Level level, int spellLevel, LivingEntity caster, CastSource castSource, MagicData playerMagicData) {
      int duration = this.getDuration(spellLevel, caster);
      int amplifier = 0;
      AABB searchBox = caster.getBoundingBox().inflate(this.getRadius(spellLevel));

      for (Mob mob : level.getEntitiesOfClass(Mob.class, searchBox, Entity::isAlive)) {
         mob.addEffect(new MobEffectInstance(GSEffects.ASTRAL_PATH_REVEAL.get(), duration, amplifier, false, false, false));
         mob.addEffect(new MobEffectInstance(MobEffects.GLOWING, 10, amplifier, false, false, false));
      }

      if (caster.isCrouching()) {
         caster.addEffect(new MobEffectInstance(GSEffects.ASTRAL_SENSE_TREASURE.get(), duration, amplifier, false, false, false));
      } else {
         caster.addEffect(new MobEffectInstance(GSEffects.ASTRAL_SENSE.get(), duration, amplifier, false, false, false));
      }

      CameraShakeManager.addCameraShake(new CameraShakeData(20, caster.position(), 8.0F));
      super.onCast(level, spellLevel, caster, castSource, playerMagicData);
   }

   private int getDuration(int spellLevel, LivingEntity caster) {
      return (int)(800.0F + this.getSpellPower(spellLevel, caster) * 100.0F);
   }

   private double getRadius(int spellLevel) {
      return 20 + spellLevel * 5;
   }

   public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
      return List.of(
         Component.translatable("ui.gametechbcs_spellbooks.radius", new Object[]{Utils.stringTruncation(this.getRadius(spellLevel), 2)}),
         Component.translatable("ui.gametechbcs_spellbooks.duration", new Object[]{Utils.timeFromTicks(this.getDuration(spellLevel, caster), 2)}),
         Component.literal("§9GTBC's Spellbooks")
      );
   }
}
