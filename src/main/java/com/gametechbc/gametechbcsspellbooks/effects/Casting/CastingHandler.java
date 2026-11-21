package com.gametechbc.gametechbcsspellbooks.effects.Casting;

import com.gametechbc.gametechbcsspellbooks.init.GSEffects;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;

@EventBusSubscriber(
   modid = "gametechbcs_spellbooks"
)
public class CastingHandler {
   public CastingHandler() {
   }

   @SubscribeEvent
   public static void onPlayerCast(SpellPreCastEvent event) {
      if (event.getEntity() instanceof ServerPlayer player && !player.level().isClientSide) {
         MobEffectInstance blackoutEffect = player.getEffect(GSEffects.CASTING.get());
         if (blackoutEffect != null) {
            event.setCanceled(true);
            player.displayClientMessage(Component.translatable("effect.gametechbcs_spellbooks.casting.warning").withStyle(ChatFormatting.RED), true);
         }
      }
   }

   @SubscribeEvent
   public static void onPlayerRightClickItem(RightClickItem event) {
      if (event.getEntity() instanceof ServerPlayer player && !player.level().isClientSide) {
         MobEffectInstance blackoutEffect = player.getEffect(GSEffects.CASTING.get());
         if (blackoutEffect != null) {
            event.setCanceled(true);
            player.displayClientMessage(Component.translatable("effect.gametechbcs_spellbooks.casting.warning").withStyle(ChatFormatting.RED), true);
         }
      }
   }

   @SubscribeEvent
   public static void onPlayerRightClickBlock(RightClickBlock event) {
      if (event.getEntity() instanceof ServerPlayer player && !player.level().isClientSide) {
         MobEffectInstance blackoutEffect = player.getEffect(GSEffects.CASTING.get());
         if (blackoutEffect != null) {
            event.setCanceled(true);
            player.displayClientMessage(Component.translatable("effect.gametechbcs_spellbooks.casting.warning").withStyle(ChatFormatting.RED), true);
         }
      }
   }
}
