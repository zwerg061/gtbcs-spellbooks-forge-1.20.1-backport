package com.gametechbc.gametechbcsspellbooks.effects.Blackout;

import com.gametechbc.gametechbcsspellbooks.init.GSEffects;
import io.redspace.ironsspellbooks.api.events.ChangeManaEvent;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;

@EventBusSubscriber(
   modid = "gametechbcs_spellbooks"
)
public class BlackoutHandler {
   public BlackoutHandler() {
   }

   @SubscribeEvent
   public static void onPlayerCast(SpellPreCastEvent event) {
      if (event.getEntity() instanceof ServerPlayer player && !player.level().isClientSide) {
         MobEffectInstance blackoutEffect = player.getEffect(GSEffects.BLACKOUT.get());
         if (blackoutEffect != null) {
            event.setCanceled(true);
            player.displayClientMessage(Component.literal("Blackout Active: Spells are suppressed!").withStyle(ChatFormatting.RED), true);
         }
      }
   }

   @SubscribeEvent
   public static void onPlayerRightClickItem(RightClickItem event) {
      if (event.getEntity() instanceof ServerPlayer player && !player.level().isClientSide) {
         MobEffectInstance blackoutEffect = player.getEffect(GSEffects.BLACKOUT.get());
         if (blackoutEffect != null) {
            event.setCanceled(true);
            player.displayClientMessage(Component.literal("Blackout Active: Cannot use items!").withStyle(ChatFormatting.RED), true);
         }
      }
   }

   @SubscribeEvent
   public static void onManaRegen(ChangeManaEvent event) {
      if (event.getEntity().hasEffect(GSEffects.BLACKOUT.get())) {
         event.setCanceled(true);
      }
   }
}
