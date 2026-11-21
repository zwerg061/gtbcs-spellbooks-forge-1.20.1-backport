package com.gametechbc.gametechbcsspellbooks.item.imbued_echos;

import com.gametechbc.spelllib.item.AdvancedEchoCurio;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.Item.Properties;

/**
 * 1.20.1 port: SpellLib 3.0.0 renamed AbstractRandomImbuingAttributeCurio to
 * AdvancedEchoCurio and replaced LevelRange with a 0..1 quality range.
 */
public class BloodEchoCurio extends AdvancedEchoCurio {
   public BloodEchoCurio(Properties properties) {
      super(properties, getAttributes());
   }

   /** Converts the original [minLevel,maxLevel] range into SpellLib's quality range. */
   private static SpellAttributes attrs(AbstractSpell spell, int minLevel, int maxLevel, int weight, boolean prioritizeLowerLevel) {
      int max = Math.max(2, spell.getMaxLevel());
      float qualityMin = (float)(minLevel - 1) / (float)(max - 1);
      float qualityMax = (float)(maxLevel - 1) / (float)(max - 1);
      return new SpellAttributes(weight, prioritizeLowerLevel, Math.min(qualityMin, 1.0F), Math.min(qualityMax, 1.0F));
   }

   protected Map<AbstractSpell, SpellAttributes> getSpellAttributes() {
      Map<AbstractSpell, SpellAttributes> spells = new LinkedHashMap<>();
      spells.put(SpellRegistry.ACUPUNCTURE_SPELL.get(), attrs(SpellRegistry.ACUPUNCTURE_SPELL.get(), 1, 10, 10, true));
      spells.put(SpellRegistry.BLOOD_NEEDLES_SPELL.get(), attrs(SpellRegistry.BLOOD_NEEDLES_SPELL.get(), 1, 10, 15, true));
      spells.put(SpellRegistry.BLOOD_SLASH_SPELL.get(), attrs(SpellRegistry.BLOOD_SLASH_SPELL.get(), 1, 5, 8, true));
      spells.put(SpellRegistry.BLOOD_STEP_SPELL.get(), attrs(SpellRegistry.BLOOD_STEP_SPELL.get(), 1, 5, 6, false));
      spells.put(SpellRegistry.DEVOUR_SPELL.get(), attrs(SpellRegistry.DEVOUR_SPELL.get(), 1, 10, 12, true));
      spells.put(SpellRegistry.HEARTSTOP_SPELL.get(), attrs(SpellRegistry.HEARTSTOP_SPELL.get(), 1, 10, 3, false));
      spells.put(SpellRegistry.RAISE_DEAD_SPELL.get(), attrs(SpellRegistry.RAISE_DEAD_SPELL.get(), 1, 6, 7, true));
      spells.put(SpellRegistry.RAY_OF_SIPHONING_SPELL.get(), attrs(SpellRegistry.RAY_OF_SIPHONING_SPELL.get(), 1, 10, 6, false));
      spells.put(SpellRegistry.SACRIFICE_SPELL.get(), attrs(SpellRegistry.SACRIFICE_SPELL.get(), 1, 5, 8, true));
      spells.put(SpellRegistry.WITHER_SKULL_SPELL.get(), attrs(SpellRegistry.WITHER_SKULL_SPELL.get(), 1, 10, 7, true));
      return spells;
   }

   protected Component getUnassignedHoverText() {
      return Component.translatable("item.traveloptics.random_imbue_curio.blood_unassigned.tooltip");
   }

   protected Component getAssignedHoverText() {
      return Component.translatable("item.traveloptics.random_imbue_curio.blood_assigned.tooltip").withStyle(ChatFormatting.DARK_GREEN);
   }

   protected SoundEvent getAssignSound() {
      return SoundRegistry.BLOOD_CAST.get();
   }

   private static Map<Attribute, AttributeModifier> getAttributes() {
      Map<Attribute, AttributeModifier> attributes = new LinkedHashMap<>();
      attributes.put(
         AttributeRegistry.BLOOD_SPELL_POWER.get(),
         new AttributeModifier("traveloptics:blood_spell_power", 0.15, Operation.MULTIPLY_BASE)
      );
      return attributes;
   }
}
