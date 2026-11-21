package com.gametechbc.gametechbcsspellbooks.effects.Casting;

import com.gametechbc.gametechbcsspellbooks.init.GSEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.client.event.ComputeFovModifierEvent;

@EventBusSubscriber(
   modid = "gametechbcs_spellbooks",
   value = {Dist.CLIENT}
)
public class CastingClientHandler {
   public CastingClientHandler() {
   }

   @SubscribeEvent
   public static void onFovModifierEvent(ComputeFovModifierEvent event) {
      Player entity = event.getPlayer();
      if (entity.hasEffect(GSEffects.CASTING.get())) {
         event.setNewFovModifier(1.0F);
      }
   }
}
