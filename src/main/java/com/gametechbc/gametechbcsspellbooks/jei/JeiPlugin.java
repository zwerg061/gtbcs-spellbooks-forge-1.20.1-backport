package com.gametechbc.gametechbcsspellbooks.jei;

import com.gametechbc.gametechbcsspellbooks.init.GSItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {
   public JeiPlugin() {
   }

   public ResourceLocation getPluginUid() {
      return new ResourceLocation("gametechbcs_spellbooks", "jei_plugin");
   }

   public void registerRecipes(IRecipeRegistration registration) {
      registration.addItemStackInfo(
         new ItemStack((ItemLike)GSItems.AMULET_OF_SPECTRAL_SHIFT.get()),
         new Component[]{Component.translatable("item.gametechbcs_spellbooks.amulet_of_spectral_shift.guide")}
      );
      registration.addItemStackInfo(
         new ItemStack((ItemLike)GSItems.GTBCS_MAGICAL_REPOSITORY.get()),
         new Component[]{Component.translatable("item.gametechbcs_spellbooks.gtbcs_magical_repository.guide")}
      );
   }
}
