package com.gametechbc.gametechbcsspellbooks.spells.nature;

import com.gametechbc.gametechbcsspellbooks.api.spells.GSSpellAnimations;
import com.gametechbc.gametechbcsspellbooks.init.GSEffects;
import com.gametechbc.gametechbcsspellbooks.init.GSSounds;
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
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

@AutoSpellConfig
public class AerialCollapseSpell extends AbstractSpell {
   private final ResourceLocation spellId = new ResourceLocation("gametechbcs_spellbooks", "aerial_collapse");
   private final DefaultConfig defaultConfig = new DefaultConfig()
      .setMinRarity(SpellRarity.COMMON)
      .setSchoolResource(SchoolRegistry.NATURE_RESOURCE)
      .setMaxLevel(5)
      .setCooldownSeconds(45.0)
      .build();

   public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
      int effectLevel = (int)this.getDamage(spellLevel, caster);
      return List.of(
         Component.translatable("ui.gametechbcs_spellbooks.radius", new Object[]{Utils.stringTruncation(this.getRadius(spellLevel, caster), 2)}),
         Component.translatable("ui.gametechbcs_spellbooks.aerial_collapse_level", new Object[]{effectLevel}),
         Component.literal("§9GTBC's Spellbooks")
      );
   }

   public AerialCollapseSpell() {
      this.manaCostPerLevel = 80;
      this.baseSpellPower = 1;
      this.spellPowerPerLevel = 1;
      this.castTime = 0;
      this.baseManaCost = 80;
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
      return Optional.of((SoundEvent)GSSounds.AERIAL_COLLAPSE.get());
   }

   public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
      float radius = this.getRadius(spellLevel, entity);
      int effectLevel = (int)this.getDamage(spellLevel, entity);
      level.getEntities(
            entity,
            entity.getBoundingBox().inflate(radius, 4.0, radius),
            target -> !DamageSources.isFriendlyFireBetween(target, entity) && Utils.hasLineOfSight(level, entity, target, true)
         )
         .forEach(target -> {
            if (target instanceof LivingEntity livingEntity && livingEntity.distanceToSqr(entity) < radius * radius) {
               livingEntity.addEffect(new MobEffectInstance(GSEffects.AERIAL_COLLAPSE.get(), 100, effectLevel - 1));
            }
         });
      entity.addEffect(new MobEffectInstance(GSEffects.CASTING.get(), 60, 2, false, false, false));
      entity.addEffect(new MobEffectInstance(GSEffects.AERIAL_COLLAPSE_HELPER.get(), 60, 2, false, false, false));
      MagicManager.spawnParticles(
         level,
         new BlastwaveParticleOptions(((SchoolType)SchoolRegistry.NATURE.get()).getTargetingColor(), radius),
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
      super.onCast(level, spellLevel, entity, castSource, playerMagicData);
   }

   public float getRadius(int spellLevel, LivingEntity caster) {
      return 2 + spellLevel * 2;
   }

   public float getDamage(int spellLevel, LivingEntity caster) {
      return 5.0F + this.getSpellPower(spellLevel, caster) * 4.0F;
   }

   public AnimationHolder getCastFinishAnimation() {
      return GSSpellAnimations.AERIAL_COLLAPSE;
   }
}
