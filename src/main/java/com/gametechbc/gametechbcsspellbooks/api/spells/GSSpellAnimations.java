package com.gametechbc.gametechbcsspellbooks.api.spells;

import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import net.minecraft.resources.ResourceLocation;

public class GSSpellAnimations {
   public static ResourceLocation ANIMATION_RESOURCE = new ResourceLocation("gametechbcs_spellbooks", "animation");
   public static final AnimationHolder AERIAL_COLLAPSE = new AnimationHolder("gametechbcs_spellbooks:aerial_collapse", true, true);
   public static final AnimationHolder SPECTRAL_BLINK = new AnimationHolder("gametechbcs_spellbooks:spectral_blink", true, false);
   public static final AnimationHolder SHOOT_WINDUP = new AnimationHolder("gametechbcs_spellbooks:blood_howl_windup", true, true);
   public static final AnimationHolder SHOOT = new AnimationHolder("gametechbcs_spellbooks:blood_howl_shoot", true, true);
   public static final AnimationHolder VIGOR_SIPHON = new AnimationHolder("gametechbcs_spellbooks:vigor_siphon", true, false);
   public static final AnimationHolder NULLFLARE_START = new AnimationHolder("gametechbcs_spellbooks:nullflare_start", true, false);
   public static final AnimationHolder NULLFLARE_FINISH = new AnimationHolder("gametechbcs_spellbooks:nullflare_finish", true, false);
   public static final AnimationHolder REVERSAL = new AnimationHolder("gametechbcs_spellbooks:reversal", true);
   public static final AnimationHolder METEOR_STORM_CAST = new AnimationHolder("gametechbcs_spellbooks:meteor_storm_cast", true, true);
   public static final AnimationHolder BANISH_CHARGE = new AnimationHolder("gametechbcs_spellbooks:banish_charge", true, true);
   public static final AnimationHolder BANISH_FINISH = new AnimationHolder("gametechbcs_spellbooks:banish_finish", true, true);

   public GSSpellAnimations() {
   }
}
