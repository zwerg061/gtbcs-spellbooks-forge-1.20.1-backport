package com.gametechbc.gametechbcsspellbooks.item.spellbook;

import com.gametechbc.gametechbcsspellbooks.init.GSSpells;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.item.UniqueSpellBook;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;

public class GtbcsMagicalRepositorySpellbook extends UniqueSpellBook {
   public GtbcsMagicalRepositorySpellbook() {
      super(
         SpellDataRegistryHolder.of(
            new SpellDataRegistryHolder[]{
               new SpellDataRegistryHolder(GSSpells.NULLFLARE_SPELL, 5), new SpellDataRegistryHolder(GSSpells.AERIAL_COLLAPSE_SPELL, 5)
            }
         ),
         8
      );
      this.withSpellbookAttributes(
         new AttributeContainer[]{
            new AttributeContainer(AttributeRegistry.MAX_MANA, 200.0, Operation.ADDITION),
            new AttributeContainer(AttributeRegistry.SPELL_POWER, 0.15, Operation.MULTIPLY_BASE),
            new AttributeContainer(AttributeRegistry.COOLDOWN_REDUCTION, 0.1, Operation.MULTIPLY_BASE)
         }
      );
   }
}
