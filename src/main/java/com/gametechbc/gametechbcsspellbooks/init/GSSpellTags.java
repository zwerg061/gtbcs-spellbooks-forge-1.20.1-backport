package com.gametechbc.gametechbcsspellbooks.init;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class GSSpellTags {
   public static final TagKey<AbstractSpell> DEAD_KING_USABLE = create("dead_king_usable");

   public GSSpellTags() {
   }

   private static TagKey<AbstractSpell> create(String tagName) {
      return TagKey.create(SpellRegistry.SPELL_REGISTRY_KEY, new ResourceLocation("gametechbcs_spellbooks", tagName));
   }
}
