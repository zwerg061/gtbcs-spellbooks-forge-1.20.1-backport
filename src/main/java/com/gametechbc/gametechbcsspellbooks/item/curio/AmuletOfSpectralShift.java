package com.gametechbc.gametechbcsspellbooks.item.curio;

import com.gametechbc.gametechbcsspellbooks.api.compat.Curios;
import io.redspace.ironsspellbooks.item.curios.SimpleDescriptiveCurio;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;

public class AmuletOfSpectralShift extends SimpleDescriptiveCurio {
   public AmuletOfSpectralShift() {
      super(new Properties().stacksTo(1), Curios.NECKLACE_SLOT);
   }

   public List<Component> getDescriptionLines(ItemStack stack) {
      return List.of(this.getDescription(stack));
   }

   public Component getDescription(ItemStack stack) {
      return Component.translatable("item.gametechbcs_spellbooks.amulet_of_spectral_shift.tooltip.desc").withStyle(ChatFormatting.BLUE);
   }
}
