package com.gametechbc.gametechbcsspellbooks.spells.nature;

import com.gametechbc.gametechbcsspellbooks.entity.spells.EnsnareEntity;
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
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

@AutoSpellConfig
public class EnsnareSpell extends AbstractSpell {
   private final ResourceLocation spellId = new ResourceLocation("gametechbcs_spellbooks", "ensnare");
   private final DefaultConfig defaultConfig = new DefaultConfig()
      .setMinRarity(SpellRarity.RARE)
      .setSchoolResource(SchoolRegistry.NATURE_RESOURCE)
      .setMaxLevel(5)
      .setCooldownSeconds(50.0)
      .build();

   public EnsnareSpell() {
      this.manaCostPerLevel = 25;
      this.baseSpellPower = 1;
      this.spellPowerPerLevel = 1;
      this.castTime = 10;
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
      return Optional.of((SoundEvent)SoundRegistry.EARTHQUAKE_IMPACT.get());
   }

   public Optional<SoundEvent> getCastFinishSound() {
      return Optional.of((SoundEvent)SoundRegistry.EARTHQUAKE_CAST.get());
   }

   public boolean stopSoundOnCancel() {
      return true;
   }

   public AnimationHolder getCastStartAnimation() {
      return SpellAnimations.STOMP;
   }

   public AnimationHolder getCastFinishAnimation() {
      return AnimationHolder.pass();
   }

   public int getEffectiveCastTime(int spellLevel, @Nullable LivingEntity entity) {
      return this.getCastTime(spellLevel);
   }

   public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
      float radius = this.getRadius(spellLevel);
      int rootDuration = (int)this.getRootDuration(spellLevel, entity);
      EnsnareEntity ensnare = new EnsnareEntity(level);
      ensnare.setOwner(entity);
      ensnare.setDuration(90);
      ensnare.setMaxRootedEntities(this.getRootEntities(spellLevel));
      ensnare.setRootDuration(rootDuration);
      ensnare.setRadius(radius);
      ensnare.setCircular();
      ensnare.moveTo(entity.position());
      level.addFreshEntity(ensnare);
      super.onCast(level, spellLevel, entity, castSource, playerMagicData);
   }

   public float getRadius(int spellLevel) {
      return 6.0F + spellLevel * 2.0F;
   }

   public int getRootEntities(int spellLevel) {
      return 3 + spellLevel;
   }

   public float getRootDuration(int spellLevel, LivingEntity caster) {
      return 100.0F + this.getSpellPower(spellLevel, caster) * 20.0F;
   }

   public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
      return List.of(
         Component.translatable("ui.gametechbcs_spellbooks.radius", new Object[]{Utils.stringTruncation(this.getRadius(spellLevel), 2)}),
         Component.translatable("ui.gametechbcs_spellbooks.effect_length", new Object[]{Utils.timeFromTicks(this.getRootDuration(spellLevel, caster), 2)}),
         Component.translatable("ui.gametechbcs_spellbooks.root_limit", new Object[]{Utils.stringTruncation(this.getRootEntities(spellLevel), 2)}),
         Component.literal("§9GTBC's Spellbooks")
      );
   }
}
