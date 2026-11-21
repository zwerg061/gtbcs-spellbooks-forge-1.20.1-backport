package com.gametechbc.gametechbcsspellbooks.init;

import com.gametechbc.gametechbcsspellbooks.spells.blood.CallForthTheDeadKingSpell;
import com.gametechbc.gametechbcsspellbooks.spells.blood.CrimsonDownpourSpell;
import com.gametechbc.gametechbcsspellbooks.spells.eldritch.BlackoutSpell;
import com.gametechbc.gametechbcsspellbooks.spells.eldritch.PsychicBoltSpell;
import com.gametechbc.gametechbcsspellbooks.spells.eldritch.ReversalSpell;
import com.gametechbc.gametechbcsspellbooks.spells.eldritch.SpectralBlinkSpell;
import com.gametechbc.gametechbcsspellbooks.spells.ender.AstralSenseSpell;
import com.gametechbc.gametechbcsspellbooks.spells.ender.DisplacementSpell;
import com.gametechbc.gametechbcsspellbooks.spells.evocation.AshenBreathSpell;
import com.gametechbc.gametechbcsspellbooks.spells.evocation.LingeringStrainSpell;
import com.gametechbc.gametechbcsspellbooks.spells.fire.FlamesRebornSpell;
import com.gametechbc.gametechbcsspellbooks.spells.fire.MeteorStormSpell;
import com.gametechbc.gametechbcsspellbooks.spells.holy.NullflareSpell;
import com.gametechbc.gametechbcsspellbooks.spells.ice.ShatterpointSpell;
import com.gametechbc.gametechbcsspellbooks.spells.nature.AcidRainSpell;
import com.gametechbc.gametechbcsspellbooks.spells.nature.AerialCollapseSpell;
import com.gametechbc.gametechbcsspellbooks.spells.nature.EnsnareSpell;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

public class GSSpells {
   public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SpellRegistry.SPELL_REGISTRY_KEY, "gametechbcs_spellbooks");
   public static final RegistryObject<AbstractSpell> CALL_FORTH_THE_DEAD_KING_SPELL = registerSpell(new CallForthTheDeadKingSpell());
   public static final RegistryObject<AbstractSpell> CRIMSON_DOWNPOUR_SPELL = registerSpell(new CrimsonDownpourSpell());
   public static final RegistryObject<AbstractSpell> SPECTRAL_BLINK_SPELL = registerSpell(new SpectralBlinkSpell());
   public static final RegistryObject<AbstractSpell> REVERSAL_SPELL = registerSpell(new ReversalSpell());
   public static final RegistryObject<AbstractSpell> BLACKOUT_SPELL = registerSpell(new BlackoutSpell());
   public static final RegistryObject<AbstractSpell> PSYCHIC_BOLT_SPELL = registerSpell(new PsychicBoltSpell());
   public static final RegistryObject<AbstractSpell> DISPLACEMENT_SPELL = registerSpell(new DisplacementSpell());
   public static final RegistryObject<AbstractSpell> ASTRAL_SENSE_SPELL = registerSpell(new AstralSenseSpell());
   public static final RegistryObject<AbstractSpell> LINGERING_STRAIN_SPELL = registerSpell(new LingeringStrainSpell());
   public static final RegistryObject<AbstractSpell> ASHEN_BREATH = registerSpell(new AshenBreathSpell());
   public static final RegistryObject<AbstractSpell> METEOR_STORM_SPELL = registerSpell(new MeteorStormSpell());
   public static final RegistryObject<AbstractSpell> FLAMES_REBORN_SPELL = registerSpell(new FlamesRebornSpell());
   public static final RegistryObject<AbstractSpell> NULLFLARE_SPELL = registerSpell(new NullflareSpell());
   public static final RegistryObject<AbstractSpell> SHATTERPOINT = registerSpell(new ShatterpointSpell());
   public static final RegistryObject<AbstractSpell> AERIAL_COLLAPSE_SPELL = registerSpell(new AerialCollapseSpell());
   public static final RegistryObject<AbstractSpell> ENSNARE_SPELL = registerSpell(new EnsnareSpell());
   public static final RegistryObject<AbstractSpell> ACID_RAIN_SPELL = registerSpell(new AcidRainSpell());

   public GSSpells() {
   }

   public static RegistryObject<AbstractSpell> registerSpell(AbstractSpell spell) {
      return SPELLS.register(spell.getSpellName(), () -> spell);
   }

   public static void register(IEventBus eventBus) {
      SPELLS.register(eventBus);
   }
}
