package com.gametechbc.gametechbcsspellbooks.entity;

import com.gametechbc.gametechbcsspellbooks.entity.extended_mobs.ExtendedDeadKingBoss;
import com.gametechbc.gametechbcsspellbooks.init.GSEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(
   modid = "gametechbcs_spellbooks",
   bus = Bus.MOD
)
public class GSMobAttributes {
   public GSMobAttributes() {
   }

   @SubscribeEvent
   public static void onAttributeCreate(EntityAttributeCreationEvent event) {
      event.put((EntityType)GSEntities.EXTENDED_DEAD_KING.get(), ExtendedDeadKingBoss.deadkingAttributes().build());
   }
}
