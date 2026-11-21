package com.gametechbc.gametechbcsspellbooks.spells.blood;

import com.gametechbc.gametechbcsspellbooks.entity.extended_mobs.ExtendedDeadKingBoss;
import com.gametechbc.gametechbcsspellbooks.init.GSEntities;
import com.gametechbc.spelllib.particle.CylinderParticleManager;
import com.gametechbc.spelllib.particle.ParticleDirection;
import com.gametechbc.spelllib.spells.AbstractUniqueSpell;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@AutoSpellConfig
public class CallForthTheDeadKingSpell extends AbstractUniqueSpell {
   private final ResourceLocation spellId = new ResourceLocation("gametechbcs_spellbooks", "call_forth_the_dead_king");
   private final DefaultConfig defaultConfig = new DefaultConfig()
      .setMinRarity(SpellRarity.LEGENDARY)
      .setSchoolResource(SchoolRegistry.BLOOD_RESOURCE)
      .setMaxLevel(1)
      .setCooldownSeconds(3600.0)
      .build();

   public CallForthTheDeadKingSpell() {
      this.manaCostPerLevel = 0;
      this.baseSpellPower = 0;
      this.spellPowerPerLevel = 0;
      this.castTime = 300;
      this.baseManaCost = 15000;
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
      return Optional.of((SoundEvent)SoundRegistry.DEAD_KING_MUSIC_INTRO.get());
   }

   public Optional<SoundEvent> getCastFinishSound() {
      return Optional.of(SoundEvents.LIGHTNING_BOLT_IMPACT);
   }

   public boolean canBeInterrupted(@Nullable Player player) {
      return false;
   }

   public int getEffectiveCastTime(int spellLevel, @Nullable LivingEntity entity) {
      return this.getCastTime(spellLevel);
   }

   public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
      return List.of(Component.literal("§9GTBC's Spellbooks"));
   }

   public void onServerCastTick(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
      CylinderParticleManager.spawnParticles(level, entity, 6, ParticleHelper.BLOOD, ParticleDirection.UPWARD, 3.0, 2.0, 2.0);
      super.onServerCastTick(level, spellLevel, entity, playerMagicData);
   }

   public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
      if (!level.isClientSide) {
         ExtendedDeadKingBoss deadKing = (ExtendedDeadKingBoss)((EntityType)GSEntities.EXTENDED_DEAD_KING.get()).create(level);
         if (deadKing != null) {
            double angle = Math.toRadians(-entity.getYRot());
            double xOffset = 2.0 * Math.cos(angle - (Math.PI / 2));
            double zOffset = 2.0 * Math.sin(angle - (Math.PI / 2));
            double spawnX = entity.getX() + xOffset;
            double spawnY = entity.getY();
            double spawnZ = entity.getZ() + zOffset;
            LightningBolt lightning = (LightningBolt)EntityType.LIGHTNING_BOLT.create(level);
            if (lightning != null) {
               lightning.moveTo(spawnX, spawnY, spawnZ);
               lightning.setVisualOnly(true);
               lightning.setDamage(0.0F);
               level.addFreshEntity(lightning);
            }

            deadKing.moveTo(spawnX, spawnY, spawnZ, entity.getYRot(), entity.getXRot());
            deadKing.finalizeSpawn((ServerLevel)level, level.getCurrentDifficultyAt(deadKing.getOnPos()), MobSpawnType.MOB_SUMMONED, null, null);
            level.addFreshEntity(deadKing);
         }
      }

      CameraShakeManager.addCameraShake(new CameraShakeData(35, entity.position(), 18.0F));
      super.onCast(level, spellLevel, entity, castSource, playerMagicData);
   }

   public AnimationHolder getCastStartAnimation() {
      return SpellAnimations.ANIMATION_LONG_CAST;
   }

   public boolean stopSoundOnCancel() {
      return true;
   }
}
