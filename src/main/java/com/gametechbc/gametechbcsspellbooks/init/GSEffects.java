package com.gametechbc.gametechbcsspellbooks.init;

import com.gametechbc.gametechbcsspellbooks.effects.AerialCollapseEffect;
import com.gametechbc.gametechbcsspellbooks.effects.AerialCollapseHelperEffect;
import com.gametechbc.gametechbcsspellbooks.effects.AstralPathRevealEffect;
import com.gametechbc.gametechbcsspellbooks.effects.AstralSenseEffect;
import com.gametechbc.gametechbcsspellbooks.effects.AstralSenseTreasureEffect;
import com.gametechbc.gametechbcsspellbooks.effects.CorrodedEffect;
import com.gametechbc.gametechbcsspellbooks.effects.DisplacementEffect;
import com.gametechbc.gametechbcsspellbooks.effects.HemorrhageEffect;
import com.gametechbc.gametechbcsspellbooks.effects.PsychicControlEffect;
import com.gametechbc.gametechbcsspellbooks.effects.SpectralBlinkEffect;
import com.gametechbc.gametechbcsspellbooks.effects.ThirdPersonSwitchEffect;
import com.gametechbc.gametechbcsspellbooks.effects.Assassin.AssassinEffect;
import com.gametechbc.gametechbcsspellbooks.effects.Blackout.BlackoutEffect;
import com.gametechbc.gametechbcsspellbooks.effects.Casting.CastingEffect;
import com.gametechbc.gametechbcsspellbooks.effects.FlamesReborn.FlamesRebornEffect;
import com.gametechbc.gametechbcsspellbooks.effects.FrozenSight.FrozenSightEffect;
import com.gametechbc.gametechbcsspellbooks.effects.LingeringStrain.LingeringStrainEffect;
import com.gametechbc.gametechbcsspellbooks.effects.MeteorStorm.MeteorStormEffect;
import com.gametechbc.gametechbcsspellbooks.effects.Reversal.ReversalEffect;
import com.gametechbc.gametechbcsspellbooks.effects.WitheredStrike.WitheredStrikeEffect;
import com.gametechbc.gametechbcsspellbooks.effects.nullflare.NullflareFireEffect;
import com.gametechbc.gametechbcsspellbooks.effects.nullflare.NullflareIceEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class GSEffects {
   public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, "gametechbcs_spellbooks");
   public static final RegistryObject<MobEffect> ASSASSIN = MOB_EFFECTS.register("assassin", AssassinEffect::new);
   public static final RegistryObject<MobEffect> REVERSAL = MOB_EFFECTS.register("reversal", ReversalEffect::new);
   public static final RegistryObject<MobEffect> FROZEN_SIGHT = MOB_EFFECTS.register("frozen_sight", FrozenSightEffect::new);
   public static final RegistryObject<MobEffect> LINGERING_STRAIN = MOB_EFFECTS.register("lingering_strain", LingeringStrainEffect::new);
   public static final RegistryObject<MobEffect> AERIAL_COLLAPSE = MOB_EFFECTS.register("aerial_collapse", AerialCollapseEffect::new);
   public static final RegistryObject<MobEffect> AERIAL_COLLAPSE_HELPER = MOB_EFFECTS.register(
      "aerial_collapse_helper", AerialCollapseHelperEffect::new
   );
   public static final RegistryObject<MobEffect> SPECTRAL_BLINK = MOB_EFFECTS.register("spectral_blink", SpectralBlinkEffect::new);
   public static final RegistryObject<MobEffect> BLACKOUT = MOB_EFFECTS.register("blackout", BlackoutEffect::new);
   public static final RegistryObject<MobEffect> NULLFLARE_ICE = MOB_EFFECTS.register("nullflare_ice", NullflareIceEffect::new);
   public static final RegistryObject<MobEffect> NULLFLARE_FIRE = MOB_EFFECTS.register("nullflare_fire", NullflareFireEffect::new);
   public static final RegistryObject<MobEffect> THIRD_PERSON_SWITCH = MOB_EFFECTS.register("third_person_switch", ThirdPersonSwitchEffect::new);
   public static final RegistryObject<MobEffect> CASTING = MOB_EFFECTS.register("casting", CastingEffect::new);
   public static final RegistryObject<MobEffect> METEOR_STORM = MOB_EFFECTS.register("meteor_storm", MeteorStormEffect::new);
   public static final RegistryObject<MobEffect> WITHERED_STRIKE = MOB_EFFECTS.register("withered_strike", WitheredStrikeEffect::new);
   public static final RegistryObject<MobEffect> DISPLACEMENT = MOB_EFFECTS.register("displacement", DisplacementEffect::new);
   public static final RegistryObject<MobEffect> FLAMES_REBORN = MOB_EFFECTS.register("flames_reborn", FlamesRebornEffect::new);
   public static final RegistryObject<MobEffect> PSYCHIC_CONTROL = MOB_EFFECTS.register("psychic_control", PsychicControlEffect::new);
   public static final RegistryObject<MobEffect> ASTRAL_PATH_REVEAL = MOB_EFFECTS.register("astral_path_reveal", AstralPathRevealEffect::new);
   public static final RegistryObject<MobEffect> ASTRAL_SENSE = MOB_EFFECTS.register("astral_sense", AstralSenseEffect::new);
   public static final RegistryObject<MobEffect> ASTRAL_SENSE_TREASURE = MOB_EFFECTS.register(
      "astral_sense_treasure", AstralSenseTreasureEffect::new
   );
   public static final RegistryObject<MobEffect> CORRODED = MOB_EFFECTS.register("corroded", CorrodedEffect::new);
   public static final RegistryObject<MobEffect> HEMORRHAGE = MOB_EFFECTS.register("hemorrhage", HemorrhageEffect::new);

   public GSEffects() {
   }

   public static void register(IEventBus eventBus) {
      MOB_EFFECTS.register(eventBus);
   }
}
