package com.gametechbc.gametechbcsspellbooks;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.config.ModConfigEvent.Loading;
import net.minecraftforge.fml.event.config.ModConfigEvent.Reloading;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

@EventBusSubscriber(
   modid = "gametechbcs_spellbooks",
   bus = Bus.MOD
)
public class Config {
   private static final Builder BUILDER = new Builder();
   private static final BooleanValue LOG_DIRT_BLOCK = BUILDER.comment("Whether to log the dirt block on common setup").define("logDirtBlock", true);
   public static final IntValue MAGIC_NUMBER = BUILDER.comment("A magic number").defineInRange("magicNumber", 20, 0, Integer.MAX_VALUE);
   public static final ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER.comment("What you want the introduction message to be for the magic number")
      .define("magicNumberIntroduction", "The magic number is... ");
   private static final ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER.comment("A list of items to log on common setup.")
      .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), Config::validateItemName);
   static final ForgeConfigSpec SPEC = BUILDER.build();
   public static boolean logDirtBlock;
   public static int magicNumber;
   public static String magicNumberIntroduction;
   public static Set<Item> items;

   public Config() {
   }

   private static boolean validateItemName(Object obj) {
      return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(new ResourceLocation((String)itemName));
   }

   @SubscribeEvent
   public static void onLoad(Loading event) {
      logDirtBlock = (Boolean)LOG_DIRT_BLOCK.get();
      magicNumber = (Integer)MAGIC_NUMBER.get();
      magicNumberIntroduction = (String)MAGIC_NUMBER_INTRODUCTION.get();
      items = ((List<String>)ITEM_STRINGS.get())
         .stream()
         .map(itemName -> (Item)BuiltInRegistries.ITEM.get(new ResourceLocation((String)itemName)))
         .collect(Collectors.toSet());
   }

   @SubscribeEvent
   public static void onReload(Reloading event) {
      logDirtBlock = (Boolean)LOG_DIRT_BLOCK.get();
      magicNumber = (Integer)MAGIC_NUMBER.get();
      magicNumberIntroduction = (String)MAGIC_NUMBER_INTRODUCTION.get();
      items = ((List<String>)ITEM_STRINGS.get())
         .stream()
         .map(itemName -> (Item)BuiltInRegistries.ITEM.get(new ResourceLocation((String)itemName)))
         .collect(Collectors.toSet());
   }
}
