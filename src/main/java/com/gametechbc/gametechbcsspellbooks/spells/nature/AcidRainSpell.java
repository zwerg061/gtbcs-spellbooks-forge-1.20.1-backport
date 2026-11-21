package com.gametechbc.gametechbcsspellbooks.spells.nature;

import com.gametechbc.gametechbcsspellbooks.entity.spells.AcidRainAoe;
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
public class AcidRainSpell extends AbstractSpell {
   private final ResourceLocation spellId = new ResourceLocation("gametechbcs_spellbooks", "acid_rain");
   private final DefaultConfig defaultConfig = new DefaultConfig()
      .setMinRarity(SpellRarity.EPIC)
      .setSchoolResource(SchoolRegistry.NATURE_RESOURCE)
      .setMaxLevel(3)
      .setCooldownSeconds(30.0)
      .build();

   public AcidRainSpell() {
      this.manaCostPerLevel = 15;
      this.baseSpellPower = 1;
      this.spellPowerPerLevel = 1;
      this.castTime = 40;
      this.baseManaCost = 30;
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
      return Optional.of((SoundEvent)SoundRegistry.POISON_CAST.get());
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
            AcidRainAoe acidRainAoe = new AcidRainAoe(world);
            acidRainAoe.setOwner(entity);
            acidRainAoe.setRadius(this.getRadius(spellLevel));
            acidRainAoe.setDuration(this.getDuration() + 40);
            acidRainAoe.setCorrodedEffectAmplifier(this.getCorrodedAmplifier(spellLevel, entity));
            acidRainAoe.setCircular();
            acidRainAoe.setPos(targetEntity.position());
            world.addFreshEntity(acidRainAoe);
            super.onCast(world, spellLevel, entity, castSource, playerMagicData);
         }
      }
   }

   private float getRadius(int spellLevel) {
      return 2 + spellLevel * 2;
   }

   private int getCorrodedAmplifier(int spellLevel, LivingEntity caster) {
      return (int)(0.0F + this.getSpellPower(spellLevel, caster) * 2.0F);
   }

   private int getDuration() {
      return 100;
   }

   public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
      return List.of(
         Component.translatable("ui.gametechbcs_spellbooks.radius", new Object[]{Utils.stringTruncation(this.getRadius(spellLevel), 2)}),
         Component.translatable("ui.gametechbcs_spellbooks.duration", new Object[]{Utils.timeFromTicks(this.getDuration(), 2)}),
         Component.translatable(
            "ui.gametechbcs_spellbooks.corroded_amplifier", new Object[]{Utils.stringTruncation(this.getCorrodedAmplifier(spellLevel, caster), 1)}
         ),
         Component.literal("§9GTBC's Spellbooks")
      );
   }

   public boolean stopSoundOnCancel() {
      return true;
   }
}
