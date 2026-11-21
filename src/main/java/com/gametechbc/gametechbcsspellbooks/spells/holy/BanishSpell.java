package com.gametechbc.gametechbcsspellbooks.spells.holy;

import com.gametechbc.gametechbcsspellbooks.api.spells.GSSpellAnimations;
import com.gametechbc.gametechbcsspellbooks.init.GSSounds;
import com.gametechbc.gametechbcsspellbooks.util.GSDamageTypes;
import com.gametechbc.gametechbcsspellbooks.util.GSTags;
import com.gametechbc.spelllib.particle.CylinderParticleManager;
import com.gametechbc.spelllib.particle.ParticleDirection;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
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
import io.redspace.ironsspellbooks.capabilities.magic.RecastResult;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.particle.ShockwaveParticleOptions;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@AutoSpellConfig
public class BanishSpell extends AbstractSpell {
   private final ResourceLocation spellId = new ResourceLocation("gametechbcs_spellbooks", "banish");
   private final DefaultConfig defaultConfig = new DefaultConfig()
      .setMinRarity(SpellRarity.RARE)
      .setSchoolResource(SchoolRegistry.HOLY_RESOURCE)
      .setMaxLevel(3)
      .setCooldownSeconds(90.0)
      .build();

   public BanishSpell() {
      this.manaCostPerLevel = 50;
      this.baseSpellPower = 1;
      this.spellPowerPerLevel = 1;
      this.castTime = 39;
      this.baseManaCost = 100;
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
      return Optional.of((SoundEvent)GSSounds.BANISH_CHARGE.get());
   }

   public Optional<SoundEvent> getCastFinishSound() {
      return Optional.of((SoundEvent)GSSounds.BANISH_CAST.get());
   }

   public boolean stopSoundOnCancel() {
      return true;
   }

   public AnimationHolder getCastStartAnimation() {
      return GSSpellAnimations.BANISH_CHARGE;
   }

   public AnimationHolder getCastFinishAnimation() {
      return GSSpellAnimations.BANISH_FINISH;
   }

   public boolean canBeInterrupted(@Nullable Player player) {
      return false;
   }

   public int getEffectiveCastTime(int spellLevel, @Nullable LivingEntity entity) {
      return this.getCastTime(spellLevel);
   }

   public void onServerPreCast(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
      double radius = this.getRange(spellLevel);
      Component message = Component.translatable("spell.gametechbcs_spellbooks.banish.message.precast").withStyle(ChatFormatting.YELLOW);
      level.getEntitiesOfClass(ServerPlayer.class, entity.getBoundingBox().inflate(radius))
         .forEach(player -> player.connection.send(new ClientboundSetActionBarTextPacket(message)));
      MagicManager.spawnParticles(
         level,
         new ShockwaveParticleOptions(((SchoolType)SchoolRegistry.HOLY.get()).getTargetingColor(), -3.0F, true),
         entity.getX(),
         entity.getY(),
         entity.getZ(),
         1,
         0.0,
         0.0,
         0.0,
         0.0,
         true
      );
      super.onServerPreCast(level, spellLevel, entity, playerMagicData);
   }

   public void onServerCastTick(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
      float radius = this.getRange(spellLevel);
      CylinderParticleManager.spawnParticles(
         level, entity, 5 * spellLevel, ParticleHelper.CLEANSE_PARTICLE, ParticleDirection.UPWARD, radius, 3 * spellLevel, -1.0
      );
      CameraShakeManager.addCameraShake(new CameraShakeData(15, entity.position(), radius));
      super.onServerCastTick(level, spellLevel, entity, playerMagicData);
   }

   public void onCast(Level level, int spellLevel, LivingEntity caster, CastSource castSource, MagicData playerMagicData) {
      float radius = this.getRange(spellLevel);
      float healingAmount = this.getHealAmount(spellLevel, caster);
      MagicManager.spawnParticles(
         level,
         new BlastwaveParticleOptions(((SchoolType)SchoolRegistry.HOLY.get()).getTargetingColor(), radius),
         caster.getX(),
         caster.getY() + 0.165F,
         caster.getZ(),
         1,
         0.0,
         0.0,
         0.0,
         0.0,
         true
      );
      CameraShakeManager.addCameraShake(new CameraShakeData(15, caster.position(), radius));
      Component message = Component.translatable("spell.gametechbcs_spellbooks.banish.message.postcast").withStyle(ChatFormatting.RED);
      level.getEntitiesOfClass(ServerPlayer.class, caster.getBoundingBox().inflate(radius))
         .forEach(player -> player.connection.send(new ClientboundSetActionBarTextPacket(message)));
      level.getEntitiesOfClass(LivingEntity.class, caster.getBoundingBox().inflate(radius)).forEach(entity -> {
         if (this.isAlly(caster, entity)) {
            entity.heal(healingAmount);
         }
      });
      List<MobEffect> casterEffects = caster.getActiveEffects()
         .stream()
         .map(MobEffectInstance::getEffect)
         .filter(effect -> effect.getCategory() == MobEffectCategory.HARMFUL)
         .filter(holder -> !net.minecraft.core.registries.BuiltInRegistries.MOB_EFFECT.wrapAsHolder(holder).is(GSTags.BANISH_IMMUNE))
         .toList();
      casterEffects.forEach(caster::removeEffect);
      Holder<DamageType> damageTypeHolder = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(GSDamageTypes.BANISH);
      DamageSource customDamageSource = new DamageSource(damageTypeHolder);
      level.getEntitiesOfClass(LivingEntity.class, caster.getBoundingBox().inflate(radius))
         .forEach(
            entity -> {
               if (entity != caster) {
                  List<MobEffect> entityEffects = entity.getActiveEffects()
                     .stream()
                     .map(MobEffectInstance::getEffect)
                     .filter(effect -> effect.getCategory() == MobEffectCategory.BENEFICIAL)
                     .filter(holder -> !net.minecraft.core.registries.BuiltInRegistries.MOB_EFFECT.wrapAsHolder(holder).is(GSTags.BANISH_IMMUNE))
                     .toList();
                  entityEffects.forEach(entity::removeEffect);
                  if (entity.getType().is(GSTags.BANISH_INSTAKILLS)) {
                     entity.remove(RemovalReason.KILLED);
                  } else if (entity.getType().is(GSTags.BANISHABLE_ENTITY)) {
                     entity.hurt(customDamageSource, Float.MAX_VALUE);
                  }

                  if (entity instanceof AntiMagicSusceptible antiMagicSusceptible) {
                     if (antiMagicSusceptible instanceof IMagicSummon summon) {
                        if (summon.getSummoner() == caster) {
                           if (summon instanceof Mob mob && mob.getTarget() == null) {
                              antiMagicSusceptible.onAntiMagic(playerMagicData);
                           }
                        } else {
                           antiMagicSusceptible.onAntiMagic(playerMagicData);
                        }
                     } else {
                        antiMagicSusceptible.onAntiMagic(playerMagicData);
                     }
                  } else if (entity instanceof ServerPlayer serverPlayer) {
                     Utils.serverSideCancelCast(serverPlayer, true);
                     MagicData.getPlayerMagicData(serverPlayer).getPlayerRecasts().removeAll(RecastResult.COUNTERSPELL);
                  } else if (entity instanceof IMagicEntity abstractSpellCastingMob) {
                     abstractSpellCastingMob.cancelCast();
                  }
               }
            }
         );
      super.onCast(level, spellLevel, caster, castSource, playerMagicData);
   }

   public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
      float range = this.getRange(spellLevel);
      float healAmount = this.getHealAmount(spellLevel, caster);
      return List.of(
         Component.translatable("ui.gametechbcs_spellbooks.radius", new Object[]{Utils.stringTruncation(range, 2)}),
         Component.translatable("ui.gametechbcs_spellbooks.heal", new Object[]{Utils.stringTruncation(healAmount, 2)}),
         Component.literal("§9GTBC's Spellbooks")
      );
   }

   private float getRange(int spellLevel) {
      return spellLevel * 4;
   }

   private float getHealAmount(int spellLevel, LivingEntity caster) {
      return 4.0F + this.getSpellPower(spellLevel, caster) * 4.0F;
   }

   private boolean isAlly(LivingEntity caster, LivingEntity target) {
      return target == caster || target instanceof TamableAnimal tamable && tamable.isTame() && tamable.getOwner() == caster;
   }
}
