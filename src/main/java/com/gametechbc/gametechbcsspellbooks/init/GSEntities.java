package com.gametechbc.gametechbcsspellbooks.init;

import com.gametechbc.gametechbcsspellbooks.entity.extended_mobs.ExtendedDeadKingBoss;
import com.gametechbc.gametechbcsspellbooks.entity.spells.AcidRainAoe;
import com.gametechbc.gametechbcsspellbooks.entity.spells.AshenBreathProjectile;
import com.gametechbc.gametechbcsspellbooks.entity.spells.BlackoutAntiMagicField;
import com.gametechbc.gametechbcsspellbooks.entity.spells.CrimsonDownpourAoe;
import com.gametechbc.gametechbcsspellbooks.entity.spells.EnsnareEntity;
import com.gametechbc.gametechbcsspellbooks.entity.spells.psychic_bolt.PsychicBoltProjectile;
import com.gametechbc.gametechbcsspellbooks.entity.spells.reversal.ReversalEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class GSEntities {
   private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, "gametechbcs_spellbooks");
   public static final RegistryObject<EntityType<ReversalEntity>> REVERSAL = ENTITIES.register(
      "reversal",
      () -> Builder.<ReversalEntity>of(ReversalEntity::new, MobCategory.MISC)
         .sized(5.0F, 1.0F)
         .clientTrackingRange(64)
         .build(new ResourceLocation("gametechbcs_spellbooks", "reversal").toString())
   );
   public static final RegistryObject<EntityType<AshenBreathProjectile>> ASHEN_BREATH_PROJECTILE = ENTITIES.register(
      "ashen_breath",
      () -> Builder.<AshenBreathProjectile>of(AshenBreathProjectile::new, MobCategory.MISC)
         .sized(1.0F, 1.0F)
         .clientTrackingRange(64)
         .build(new ResourceLocation("gametechbcs_spellbooks", "ashen_breath").toString())
   );
   public static final RegistryObject<EntityType<ExtendedDeadKingBoss>> EXTENDED_DEAD_KING = ENTITIES.register(
      "hard_dead_king",
      () -> Builder.<ExtendedDeadKingBoss>of(ExtendedDeadKingBoss::new, MobCategory.MONSTER)
         .sized(0.9F, 3.5F)
         .clientTrackingRange(64)
         .build(new ResourceLocation("gametechbcs_spellbooks", "hard_dead_king").toString())
   );
   public static final RegistryObject<EntityType<BlackoutAntiMagicField>> BLACKOUT_ANTI_MAGIC_FIELD = ENTITIES.register(
      "blackout_anti_magic_field",
      () -> Builder.<BlackoutAntiMagicField>of(BlackoutAntiMagicField::new, MobCategory.MISC)
         .sized(4.0F, 1.2F)
         .clientTrackingRange(64)
         .build(new ResourceLocation("gametechbcs_spellbooks", "blackout_anti_magic_field").toString())
   );
   public static final RegistryObject<EntityType<AcidRainAoe>> ACID_RAIN_AOE = ENTITIES.register(
      "acid_rain_aoe",
      () -> Builder.<AcidRainAoe>of(AcidRainAoe::new, MobCategory.MISC)
         .sized(4.0F, 1.2F)
         .clientTrackingRange(64)
         .build(new ResourceLocation("gametechbcs_spellbooks", "acid_rain_aoe").toString())
   );
   public static final RegistryObject<EntityType<CrimsonDownpourAoe>> BLOOD_RAIN_AOE = ENTITIES.register(
      "blood_rain_aoe",
      () -> Builder.<CrimsonDownpourAoe>of(CrimsonDownpourAoe::new, MobCategory.MISC)
         .sized(4.0F, 1.2F)
         .clientTrackingRange(64)
         .build(new ResourceLocation("gametechbcs_spellbooks", "blood_rain_aoe").toString())
   );
   public static final RegistryObject<EntityType<EnsnareEntity>> ENSNARE = ENTITIES.register(
      "ensnare",
      () -> Builder.<EnsnareEntity>of(EnsnareEntity::new, MobCategory.MISC)
         .sized(4.0F, 1.2F)
         .clientTrackingRange(64)
         .build(new ResourceLocation("gametechbcs_spellbooks", "ensnare").toString())
   );
   public static final RegistryObject<EntityType<PsychicBoltProjectile>> PSYCHIC_BOLT_PROJECTILE = ENTITIES.register(
      "psychic_bolt_projectile",
      () -> Builder.<PsychicBoltProjectile>of(PsychicBoltProjectile::new, MobCategory.MISC)
         .sized(0.5F, 0.5F)
         .clientTrackingRange(64)
         .build(new ResourceLocation("gametechbcs_spellbooks", "psychic_bolt_projectile").toString())
   );

   public GSEntities() {
   }

   public static void register(IEventBus eventBus) {
      ENTITIES.register(eventBus);
   }
}
