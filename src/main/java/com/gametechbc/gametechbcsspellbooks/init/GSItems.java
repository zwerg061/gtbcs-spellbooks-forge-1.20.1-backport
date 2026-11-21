package com.gametechbc.gametechbcsspellbooks.init;

import com.gametechbc.gametechbcsspellbooks.item.curio.AmuletOfSpectralShift;
import com.gametechbc.gametechbcsspellbooks.item.spellbook.GtbcsMagicalRepositorySpellbook;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import java.util.Collection;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class GSItems {
   private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, "gametechbcs_spellbooks");
   public static final RegistryObject<Item> GTBCS_MAGICAL_REPOSITORY = ITEMS.register("gtbcs_magical_repository", GtbcsMagicalRepositorySpellbook::new);
   public static final Supplier<CurioBaseItem> AMULET_OF_SPECTRAL_SHIFT = ITEMS.register("amulet_of_spectral_shift", AmuletOfSpectralShift::new);

   public GSItems() {
   }

   public static void register(IEventBus eventBus) {
      ITEMS.register(eventBus);
   }

   public static Collection<RegistryObject<Item>> getItems() {
      return ITEMS.getEntries();
   }
}
