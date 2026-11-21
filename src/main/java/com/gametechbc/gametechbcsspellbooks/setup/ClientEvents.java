package com.gametechbc.gametechbcsspellbooks.setup;

import com.gametechbc.gametechbcsspellbooks.init.GSEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.client.event.ViewportEvent.ComputeFogColor;

@EventBusSubscriber(
   modid = "gametechbcs_spellbooks",
   value = {Dist.CLIENT}
)
public class ClientEvents {
   private static final float[] VIOLET_SKY_COLOR = new float[]{0.25F, 0.0F, 0.45F};
   private static final float[] HEMORRHAGE_SKY_COLOR = new float[]{0.3F, 0.0F, 0.0F};

   public ClientEvents() {
   }

   @SubscribeEvent
   public static void onSkyRender(ComputeFogColor event) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.player != null) {
         MobEffectInstance effect = mc.player.getEffect(GSEffects.ASTRAL_SENSE_TREASURE.get());
         if (effect != null) {
            event.setRed(VIOLET_SKY_COLOR[0]);
            event.setGreen(VIOLET_SKY_COLOR[1]);
            event.setBlue(VIOLET_SKY_COLOR[2]);
         }
      }

      if (mc.player != null) {
         MobEffectInstance effect = mc.player.getEffect(GSEffects.ASTRAL_SENSE.get());
         if (effect != null) {
            event.setRed(VIOLET_SKY_COLOR[0]);
            event.setGreen(VIOLET_SKY_COLOR[1]);
            event.setBlue(VIOLET_SKY_COLOR[2]);
         }
      }

      if (mc.player != null) {
         MobEffectInstance effect = mc.player.getEffect(GSEffects.HEMORRHAGE.get());
         if (effect != null) {
            event.setRed(HEMORRHAGE_SKY_COLOR[0]);
            event.setGreen(HEMORRHAGE_SKY_COLOR[1]);
            event.setBlue(HEMORRHAGE_SKY_COLOR[2]);
         }
      }
   }
}
