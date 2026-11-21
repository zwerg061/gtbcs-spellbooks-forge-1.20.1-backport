package com.gametechbc.gametechbcsspellbooks.spells.ice;

import com.gametechbc.gametechbcsspellbooks.init.GSSounds;
import com.gametechbc.gametechbcsspellbooks.particle.GSParticleHelper;
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
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

@AutoSpellConfig
public class ShatterpointSpell extends AbstractSpell {
   private final ResourceLocation spellId = new ResourceLocation("gametechbcs_spellbooks", "shatterpoint");
   private final DefaultConfig defaultConfig = new DefaultConfig()
      .setMinRarity(SpellRarity.RARE)
      .setSchoolResource(SchoolRegistry.ICE_RESOURCE)
      .setMaxLevel(5)
      .setCooldownSeconds(22.0)
      .build();

   public ShatterpointSpell() {
      this.manaCostPerLevel = 40;
      this.baseSpellPower = 1;
      this.spellPowerPerLevel = 1;
      this.castTime = 30;
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

   public AnimationHolder getCastStartAnimation() {
      return SpellAnimations.CHARGE_SPIT_ANIMATION;
   }

   public Optional<SoundEvent> getCastStartSound() {
      return Optional.of(SoundEvents.WARDEN_SONIC_CHARGE);
   }

   public Optional<SoundEvent> getCastFinishSound() {
      return Optional.of((SoundEvent)GSSounds.SHATTERPOINT_CAST.get());
   }

   public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
      float damage = this.getDamage(spellLevel, entity);
      CameraShakeManager.addCameraShake(new CameraShakeData(10, entity.position(), 25.0F));
      HitResult hitResult = Utils.raycastForEntity(level, entity, getRange(), true, 0.25F);
      if (hitResult.getType() == Type.ENTITY) {
         Entity target = ((EntityHitResult)hitResult).getEntity();
         if (target instanceof LivingEntity livingTarget) {
            DamageSources.applyDamage(livingTarget, damage, this.getDamageSource(entity));
            AABB area = new AABB(target.blockPosition()).inflate(3.0);

            for (LivingEntity nearby : level.getEntitiesOfClass(LivingEntity.class, area, e -> e != entity && e != livingTarget)) {
               DamageSources.applyDamage(nearby, damage, this.getDamageSource(entity));
               MagicManager.spawnParticles(level, ParticleHelper.SNOWFLAKE, nearby.getX(), nearby.getY(), nearby.getZ(), 40, 0.0, 0.0, 0.0, 0.3, false);
            }
         }
      }

      float distance = (float)hitResult.distanceTo(entity);
      Vec3 vec3 = entity.getLookAngle().normalize();

      for (int i = 0; i < distance; i++) {
         Vec3 vec32 = vec3.scale(i).add(entity.getEyePosition());
         MagicManager.spawnParticles(level, GSParticleHelper.FROST_BLAST, vec32.x, vec32.y, vec32.z, 1, 0.0, 0.0, 0.0, 0.0, false);
      }

      MagicManager.spawnParticles(
         level, ParticleHelper.SNOW_DUST, hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z, 50, 0.0, 0.0, 0.0, 0.2, false
      );
      super.onCast(level, spellLevel, entity, castSource, playerMagicData);
   }

   public static float getRange() {
      return 30.0F;
   }

   private float getDamage(int spellLevel, LivingEntity caster) {
      return 17.0F + this.getSpellPower(spellLevel, caster) * 3.0F;
   }

   public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
      return List.of(
         Component.translatable("ui.gametechbcs_spellbooks.aoe_damage", new Object[]{this.getDamage(spellLevel, caster)}),
         Component.translatable("ui.gametechbcs_spellbooks.range", new Object[]{Utils.stringTruncation(getRange(), 2)}),
         Component.literal("§9GTBC's Spellbooks")
      );
   }
}
