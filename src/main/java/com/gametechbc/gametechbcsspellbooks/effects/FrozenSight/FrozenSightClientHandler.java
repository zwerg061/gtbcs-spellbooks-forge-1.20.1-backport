package com.gametechbc.gametechbcsspellbooks.effects.FrozenSight;

import com.gametechbc.gametechbcsspellbooks.init.GSEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(
   modid = "gametechbcs_spellbooks",
   value = {Dist.CLIENT}
)
public class FrozenSightClientHandler {
   private static float lockedYaw;
   private static float lockedPitch;
   private static boolean isLocked = false;

   public FrozenSightClientHandler() {
   }

   @SubscribeEvent
   public static void onRenderLevelStage(RenderLevelStageEvent event) {
      Minecraft minecraft = Minecraft.getInstance();
      Player player = minecraft.player;
      if (player != null) {
         MobEffectInstance effectInstance = player.getEffect(GSEffects.FROZEN_SIGHT.get());
         if (effectInstance != null) {
            if (!isLocked) {
               lockedYaw = player.getYRot();
               lockedPitch = player.getXRot();
               isLocked = true;
            }

            player.setYRot(lockedYaw);
            player.setXRot(lockedPitch);
         } else {
            isLocked = false;
         }
      }
   }
}
