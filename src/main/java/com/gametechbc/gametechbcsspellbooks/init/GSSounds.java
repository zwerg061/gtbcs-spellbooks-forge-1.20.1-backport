package com.gametechbc.gametechbcsspellbooks.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class GSSounds {
   private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, "gametechbcs_spellbooks");
   public static RegistryObject<SoundEvent> AERIAL_COLLAPSE = registerSoundEvent("aerial_collapse");
   public static RegistryObject<SoundEvent> SPECTRAL_BLINK_FAILED = registerSoundEvent("spectral_blink");
   public static RegistryObject<SoundEvent> SPECTRAL_BLINK_CHARGE = registerSoundEvent("spectral_blink_charge");
   public static RegistryObject<SoundEvent> SPECTRAL_BLINK_SUCCESS = registerSoundEvent("spectral_blink_success");
   public static RegistryObject<SoundEvent> MANIFESTATION_CHANGE = registerSoundEvent("manifestation");
   public static RegistryObject<SoundEvent> NULLFLARE = registerSoundEvent("nullflare");
   public static RegistryObject<SoundEvent> NULLFLARE_TWO = registerSoundEvent("nullflare_two");
   public static RegistryObject<SoundEvent> REVERSAL_TRIGGER = registerSoundEvent("reversal_trigger");
   public static RegistryObject<SoundEvent> REVERSAL = registerSoundEvent("reversal");
   public static RegistryObject<SoundEvent> REFINED_REVENANT_BREATH = registerSoundEvent("refined_revenant_breath");
   public static RegistryObject<SoundEvent> BANISH_CHARGE = registerSoundEvent("banish_charge");
   public static RegistryObject<SoundEvent> BANISH_CAST = registerSoundEvent("banish_cast");
   public static RegistryObject<SoundEvent> ABYSSAL_STRIKE_TRIGGER = registerSoundEvent("abyssal_strike_trigger");
   public static RegistryObject<SoundEvent> DISPLACEMENT = registerSoundEvent("displacement");
   public static RegistryObject<SoundEvent> FLAMES_REBORN_REAPPEAR = registerSoundEvent("flames_reborn_reappear");
   public static RegistryObject<SoundEvent> FLAMES_REBORN_REVIVE = registerSoundEvent("flames_reborn_revive");
   public static RegistryObject<SoundEvent> SHATTERPOINT_CAST = registerSoundEvent("shatterpoint_cast");
   public static RegistryObject<SoundEvent> ENSNARE_PREPARE = registerSoundEvent("ensnare_prepare");
   public static RegistryObject<SoundEvent> ENSNARE_TRIGGER = registerSoundEvent("ensnare_trigger");
   public static RegistryObject<SoundEvent> ORBITAL_VOID_PULSE = registerSoundEvent("orbital_void_pulse");
   public static RegistryObject<SoundEvent> ACID_RAIN_ACTIVE = registerSoundEvent("acid_rain_active");

   public GSSounds() {
   }

   public static void register(IEventBus eventBus) {
      SOUND_EVENTS.register(eventBus);
   }

   private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
      return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("gametechbcs_spellbooks", name)));
   }
}
