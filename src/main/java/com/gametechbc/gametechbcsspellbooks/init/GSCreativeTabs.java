package com.gametechbc.gametechbcsspellbooks.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class GSCreativeTabs {
   private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "gametechbcs_spellbooks");
   public static final RegistryObject<CreativeModeTab> MAIN_CREATIVE_TAB = TABS.register(
      "main_creative_tab",
      () -> CreativeModeTab.builder()
         .title(Component.translatable("itemGroup.gametechbcs_spellbooks.main_creative_tab"))
         .icon(() -> new ItemStack((ItemLike)GSItems.GTBCS_MAGICAL_REPOSITORY.get()))
         .displayItems((enabledFeatures, entries) -> {
            entries.accept((ItemLike)GSItems.GTBCS_MAGICAL_REPOSITORY.get());
            entries.accept((ItemLike)GSItems.AMULET_OF_SPECTRAL_SHIFT.get());
         })
         .build()
   );

   public GSCreativeTabs() {
   }

   public static void register(IEventBus eventBus) {
      TABS.register(eventBus);
   }
}
