package com.gametechbc.gametechbcsspellbooks.api;

import com.gametechbc.gametechbcsspellbooks.init.GSSounds;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class SpiritPointsManager {
   private static final String SPIRIT_POINTS_KEY = "SpiritPoints";
   private static final int MAX_SPIRIT_POINTS = 200;

   public SpiritPointsManager() {
   }

   public static int getSpiritPoints(ItemStack stack) {
      return DataComponentRegistry.has(stack, DataComponentRegistry.CUSTOM_COMPONENT)
         ? DataComponentRegistry.get(stack, DataComponentRegistry.CUSTOM_COMPONENT).getInt("SpiritPoints")
         : 0;
   }

   public static void setSpiritPoints(ItemStack stack, int points) {
      int cappedPoints = Math.min(points, 200);
      CompoundTag nbt = new CompoundTag();
      nbt.putInt("SpiritPoints", cappedPoints);
      DataComponentRegistry.set(stack, DataComponentRegistry.CUSTOM_COMPONENT, nbt);
   }

   public static void addSpiritPoints(ItemStack stack, Player player, int amount) {
      int currentPoints = getSpiritPoints(stack);
      int newPoints = Math.min(currentPoints + amount, 200);
      setSpiritPoints(stack, newPoints);
      displayActionBar(player, newPoints);
      playSoundOnThreshold(player, currentPoints, newPoints);
      spawnParticlesOnThreshold(player, currentPoints, newPoints);
   }

   private static void displayActionBar(Player player, int points) {
      if (player instanceof ServerPlayer) {
         Component message = points >= 200
            ? Component.literal("☠ Soul Fragments: " + points).withStyle(style -> style.withColor(16770714)).withStyle(ChatFormatting.BOLD)
            : Component.literal("☠ Soul Fragments: " + points).withStyle(style -> style.withColor(7733218));
         player.displayClientMessage(message, true);
      }
   }

   private static void playSoundOnThreshold(Player player, int oldPoints, int newPoints) {
      if (!player.level().isClientSide()) {
         if (newPoints >= 200 && oldPoints < 200) {
            player.level().playSound(null, player.blockPosition(), (SoundEvent)GSSounds.ENSNARE_TRIGGER.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
         } else if (newPoints >= 100 && oldPoints < 100) {
            player.level().playSound(null, player.blockPosition(), (SoundEvent)GSSounds.ENSNARE_TRIGGER.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
         } else if (newPoints >= 50 && oldPoints < 50) {
            player.level().playSound(null, player.blockPosition(), (SoundEvent)GSSounds.ENSNARE_TRIGGER.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
         }
      }
   }

   private static void spawnParticlesOnThreshold(Player player, int oldPoints, int newPoints) {
      if (newPoints >= 200 && oldPoints < 200) {
         Vec3 playerPosition = player.position();
         MagicManager.spawnParticles(player.level(), ParticleHelper.EMBERS, playerPosition.x, playerPosition.y, playerPosition.z, 20, 0.0, 1.0, 0.0, 0.1, false);
      }
   }
}
