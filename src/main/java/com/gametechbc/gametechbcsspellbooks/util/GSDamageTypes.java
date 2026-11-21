package com.gametechbc.gametechbcsspellbooks.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class GSDamageTypes {
   public static final ResourceKey<DamageType> NULLFLARE_BLAST = ResourceKey.create(
      Registries.DAMAGE_TYPE, new ResourceLocation("gametechbcs_spellbooks", "nullflare_blast")
   );
   public static final ResourceKey<DamageType> NULLFLARE_FIRE = ResourceKey.create(
      Registries.DAMAGE_TYPE, new ResourceLocation("gametechbcs_spellbooks", "nullflare_fire")
   );
   public static final ResourceKey<DamageType> NULLFLARE_ICE = ResourceKey.create(
      Registries.DAMAGE_TYPE, new ResourceLocation("gametechbcs_spellbooks", "nullflare_ice")
   );
   public static final ResourceKey<DamageType> REVERSAL = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("gametechbcs_spellbooks", "reversal"));
   public static final ResourceKey<DamageType> AERIAL_COLLAPSE = ResourceKey.create(
      Registries.DAMAGE_TYPE, new ResourceLocation("gametechbcs_spellbooks", "aerial_collapse")
   );
   public static final ResourceKey<DamageType> BANISH = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("gametechbcs_spellbooks", "banish"));
   public static final ResourceKey<DamageType> BLACKOUT = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("gametechbcs_spellbooks", "blackout"));
   public static final ResourceKey<DamageType> LINGERING_STRAIN = ResourceKey.create(
      Registries.DAMAGE_TYPE, new ResourceLocation("gametechbcs_spellbooks", "lingering_strain")
   );

   public GSDamageTypes() {
   }
}
