package com.gametechbc.gametechbcsspellbooks.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class GSTags {
   public static final TagKey<MobEffect> BANISH_IMMUNE = TagKey.create(Registries.MOB_EFFECT, new ResourceLocation("gametechbcs_spellbooks", "banish_immune"));
   public static final TagKey<EntityType<?>> BANISHABLE_ENTITY = TagKey.create(
      Registries.ENTITY_TYPE, new ResourceLocation("gametechbcs_spellbooks", "banishable_entity")
   );
   public static final TagKey<EntityType<?>> BANISH_INSTAKILLS = TagKey.create(
      Registries.ENTITY_TYPE, new ResourceLocation("gametechbcs_spellbooks", "banish_instakills")
   );
   public static final TagKey<Item> CAN_CAST_REVERSAL = TagKey.create(Registries.ITEM, new ResourceLocation("gametechbcs_spellbooks", "can_cast_reversal"));
   public static final TagKey<EntityType<?>> SPECTRAL_BLINK_BLACKLIST = TagKey.create(
      Registries.ENTITY_TYPE, new ResourceLocation("gametechbcs_spellbooks", "spectral_blink_blacklist")
   );
   public static final TagKey<EntityType<?>> SPECTRAL_SHIFT_BLACKLIST = TagKey.create(
      Registries.ENTITY_TYPE, new ResourceLocation("gametechbcs_spellbooks", "spectral_shift_blacklist")
   );
   public static final TagKey<Item> SPELL_IMBUED_CURIO = TagKey.create(Registries.ITEM, new ResourceLocation("gametechbcs_spellbooks", "spell_imbued_curio"));
   public static final TagKey<Block> EXTREMELY_VALUABLES = TagKey.create(
      Registries.BLOCK, new ResourceLocation("gametechbcs_spellbooks", "extremely_valuables")
   );
   public static final TagKey<EntityType<?>> SENSED_ENTITIES = TagKey.create(
      Registries.ENTITY_TYPE, new ResourceLocation("gametechbcs_spellbooks", "sensed_entities")
   );
   public static final TagKey<EntityType<?>> APPROACH_ENTITY_BLACKLIST = TagKey.create(
      Registries.ENTITY_TYPE, new ResourceLocation("gametechbcs_spellbooks", "approach_entity_blacklist")
   );

   public GSTags() {
   }

   public static void init() {
   }
}
