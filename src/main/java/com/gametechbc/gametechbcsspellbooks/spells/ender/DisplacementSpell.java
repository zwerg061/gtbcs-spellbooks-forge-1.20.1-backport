package com.gametechbc.gametechbcsspellbooks.spells.ender;

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
import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

@AutoSpellConfig
public class DisplacementSpell extends AbstractSpell {
   private final ResourceLocation spellId = new ResourceLocation("gametechbcs_spellbooks", "displacement");
   private static final Random RANDOM = new Random();
   private final DefaultConfig defaultConfig = new DefaultConfig()
      .setMinRarity(SpellRarity.RARE)
      .setSchoolResource(SchoolRegistry.ENDER_RESOURCE)
      .setMaxLevel(3)
      .setCooldownSeconds(12.0)
      .build();

   public DisplacementSpell() {
      this.baseManaCost = 15;
      this.manaCostPerLevel = 10;
      this.baseSpellPower = 1;
      this.spellPowerPerLevel = 1;
      this.castTime = 0;
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
      return Optional.of((SoundEvent)GSSounds.DISPLACEMENT.get());
   }

   public void onCast(Level level, int spellLevel, LivingEntity caster, CastSource castSource, MagicData playerMagicData) {
      float radius = this.getRadius(spellLevel, caster);
      MagicManager.spawnParticles(
         level,
         new BlastwaveParticleOptions(((SchoolType)SchoolRegistry.ENDER.get()).getTargetingColor(), radius),
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
      CameraShakeManager.addCameraShake(new CameraShakeData(10, caster.position(), radius));
      AABB area = new AABB(caster.blockPosition()).inflate(radius);

      for (Entity entity : level.getEntities(caster, area, e -> e instanceof LivingEntity)) {
         if (entity != caster && entity instanceof LivingEntity target) {
            this.emitRandomBodyParticles((ServerLevel)level, target, 50);
            Vec3 targetLookDirection = target.getLookAngle().scale(-1.0);
            Vec3 teleportCenter = target.position().add(targetLookDirection.scale(5.0));
            double randomXOffset = (level.random.nextDouble() - 0.5) * 7.0;
            double randomZOffset = (level.random.nextDouble() - 0.5) * 7.0;
            Vec3 teleportPosition = teleportCenter.add(randomXOffset, 0.0, randomZOffset);
            target.teleportTo(teleportPosition.x, teleportPosition.y, teleportPosition.z);
            this.emitRandomBodyParticles((ServerLevel)level, target, 50);
         }
      }

      caster.addEffect(new MobEffectInstance(GSEffects.DISPLACEMENT.get(), 20, (int)(radius / 2.0F), false, false, false));
      super.onCast(level, spellLevel, caster, castSource, playerMagicData);
   }

   private void emitRandomBodyParticles(ServerLevel serverLevel, LivingEntity entity, int count) {
      for (int i = 0; i < count; i++) {
         double offsetX = (serverLevel.random.nextDouble() - 0.5) * entity.getBbWidth();
         double offsetY = serverLevel.random.nextDouble() * entity.getBbHeight();
         double offsetZ = (serverLevel.random.nextDouble() - 0.5) * entity.getBbWidth();
         serverLevel.sendParticles(
            ParticleHelper.UNSTABLE_ENDER, entity.getX() + offsetX, entity.getY() + offsetY, entity.getZ() + offsetZ, 1, 0.0, 0.0, 0.0, 0.0
         );
      }
   }

   private float getRadius(int spellLevel, LivingEntity caster) {
      return 2.0F + this.getSpellPower(spellLevel, caster) * 2.0F;
   }

   public AnimationHolder getCastFinishAnimation() {
      return SpellAnimations.SELF_CAST_ANIMATION;
   }

   public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
      return List.of(
         Component.translatable("ui.gametechbcs_spellbooks.radius", new Object[]{Utils.stringTruncation(this.getRadius(spellLevel, caster), 2)}),
         Component.literal("§9GTBC's Spellbooks")
      );
   }
}
