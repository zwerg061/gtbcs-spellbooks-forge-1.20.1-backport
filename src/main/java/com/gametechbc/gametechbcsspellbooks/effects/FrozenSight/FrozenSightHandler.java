package com.gametechbc.gametechbcsspellbooks.effects.FrozenSight;

import com.gametechbc.gametechbcsspellbooks.init.GSEffects;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;

@EventBusSubscriber(
   modid = "gametechbcs_spellbooks"
)
public class FrozenSightHandler {
   public FrozenSightHandler() {
   }

   @SubscribeEvent
   public static void onPlayerRightClickItem(RightClickItem event) {
      if (event.getEntity() instanceof ServerPlayer player && !player.level().isClientSide) {
         MobEffectInstance frozenSightEffect = player.getEffect(GSEffects.FROZEN_SIGHT.get());
         if (frozenSightEffect != null) {
            event.setCanceled(true);
            player.displayClientMessage(Component.literal("Wait till it ends!").withStyle(ChatFormatting.RED), true);
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARMOR_STAND_HIT, SoundSource.PLAYERS, 0.5F, 1.0F);
         }
      }
   }

   @SubscribeEvent
   public static void onPlayerCast(SpellPreCastEvent event) {
      if (event.getEntity() instanceof ServerPlayer player && !player.level().isClientSide) {
         MobEffectInstance frozenSightEffect = player.getEffect(GSEffects.FROZEN_SIGHT.get());
         if (frozenSightEffect != null) {
            event.setCanceled(true);
            player.displayClientMessage(Component.literal("Wait till it ends!").withStyle(ChatFormatting.RED), true);
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARMOR_STAND_HIT, SoundSource.PLAYERS, 0.5F, 1.0F);
         }
      }
   }

   @SubscribeEvent
   public static void onPlayerTick(PlayerTickEvent event) {
      Player player = event.player;
      MobEffectInstance effectInstance = player.getEffect(GSEffects.FROZEN_SIGHT.get());
      if (effectInstance != null) {
         player.setOnGround(true);
         player.setJumping(false);
      }
   }
}
