package com.gametechbc.gametechbcsspellbooks.spells.blood;

import com.gametechbc.gametechbcsspellbooks.entity.spells.CrimsonDownpourAoe;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.spells.TargetedTargetAreaCastData;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

@AutoSpellConfig
public class CrimsonDownpourSpell extends AbstractSpell {
   private final ResourceLocation spellId = new ResourceLocation("gametechbcs_spellbooks", "crimson_downpour");
   private final DefaultConfig defaultConfig = new DefaultConfig()
      .setMinRarity(SpellRarity.EPIC)
      .setSchoolResource(SchoolRegistry.BLOOD_RESOURCE)
      .setMaxLevel(3)
      .setCooldownSeconds(40.0)
      .build();

   public CrimsonDownpourSpell() {
      this.manaCostPerLevel = 40;
      this.baseSpellPower = 1;
      this.spellPowerPerLevel = 1;
      this.castTime = 40;
      this.baseManaCost = 40;
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
      return Optional.of((SoundEvent)SoundRegistry.THUNDERSTORM_PREPARE.get());
   }

   public Optional<SoundEvent> getCastFinishSound() {
      return Optional.of((SoundEvent)SoundRegistry.BLOOD_CAST.get());
   }

   public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
      if (!Utils.preCastTargetHelper(level, entity, playerMagicData, this, 32, 0.35F, false)) {
         playerMagicData.setAdditionalCastData(new TargetEntityCastData(entity));
         if (entity instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection
               .send(
                  new ClientboundSetActionBarTextPacket(
                     Component.translatable("ui.irons_spellbooks.spell_target_success_self", new Object[]{this.getDisplayName(serverPlayer)})
                        .withStyle(ChatFormatting.GREEN)
                  )
               );
         }
      }

      float radius = this.getRadius(spellLevel);
      LivingEntity target = ((TargetEntityCastData)playerMagicData.getAdditionalCastData()).getTarget((ServerLevel)level);
      TargetedAreaEntity area = TargetedAreaEntity.createTargetAreaEntity(level, target.position(), radius, Utils.packRGB(this.getTargetingColor()), target);
      playerMagicData.setAdditionalCastData(new TargetedTargetAreaCastData(target, area));
      return true;
   }

   public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
      if (playerMagicData.getAdditionalCastData() instanceof TargetedTargetAreaCastData targetData) {
         LivingEntity targetEntity = targetData.getTarget((ServerLevel)world);
         if (targetEntity != null) {
            CrimsonDownpourAoe bloodRainAoe = new CrimsonDownpourAoe(world);
            bloodRainAoe.setOwner(entity);
            bloodRainAoe.setRadius(this.getRadius(spellLevel));
            bloodRainAoe.setDuration(this.getDuration() + 40);
            bloodRainAoe.setDamage(this.getDamage(spellLevel, entity));
            bloodRainAoe.setHemorrhageEffectAmplifier(this.getCorrodedAmplifier(spellLevel, entity));
            bloodRainAoe.setLifestealAmount(0.12F);
            bloodRainAoe.setCircular();
            bloodRainAoe.setPos(targetEntity.position());
            world.addFreshEntity(bloodRainAoe);
            super.onCast(world, spellLevel, entity, castSource, playerMagicData);
         }
      }
   }

   private float getRadius(int spellLevel) {
      return 5 + spellLevel * 3;
   }

   private int getCorrodedAmplifier(int spellLevel, LivingEntity caster) {
      return (int)(3.0F + this.getSpellPower(spellLevel, caster) * 1.0F);
   }

   private float getDamage(int spellLevel, LivingEntity caster) {
      return 3.0F + this.getSpellPower(spellLevel, caster) * 1.5F;
   }

   private int getDuration() {
      return 100;
   }

   public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
      return List.of(
         Component.translatable("ui.gametechbcs_spellbooks.radius", new Object[]{Utils.stringTruncation(this.getRadius(spellLevel), 2)}),
         Component.translatable("ui.gametechbcs_spellbooks.aoe_damage", new Object[]{Utils.stringTruncation(this.getDamage(spellLevel, caster), 2)}),
         Component.translatable(
            "ui.gametechbcs_spellbooks.hemorrhage_amplifier", new Object[]{Utils.stringTruncation(this.getCorrodedAmplifier(spellLevel, caster), 1)}
         ),
         Component.literal("§9GTBC's Spellbooks")
      );
   }

   public boolean stopSoundOnCancel() {
      return true;
   }
}
