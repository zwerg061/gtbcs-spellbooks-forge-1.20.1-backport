package com.gametechbc.gametechbcsspellbooks.setup;

import com.gametechbc.gametechbcsspellbooks.init.GSEffects;
import com.gametechbc.gametechbcsspellbooks.util.GSTags;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager.SpellSelectionEvent;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.spells.SpellSlot;
import java.util.Arrays;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.event.entity.living.MobEffectEvent.Remove;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

@EventBusSubscriber(
   modid = "gametechbcs_spellbooks"
)
public class ServerEvents {
   private static final String FIRST_LOGIN_TAG = "FirstLoginTOMod";

   public ServerEvents() {
   }

   @SubscribeEvent
   public static void onPlayerLogin(PlayerLoggedInEvent event) {
      if (event.getEntity() instanceof ServerPlayer var1) {
         ;
      }
   }

   @SubscribeEvent
   public static void applyCurioBasedSpells(SpellSelectionEvent event) {
      Player player = event.getEntity();
      CuriosApi.getCuriosInventory(player).ifPresent(a -> {
         for (SlotResult i : a.findCurios(item -> item != null && ISpellContainer.isSpellContainer(item) && item.is(GSTags.SPELL_IMBUED_CURIO))) {
            ISpellContainer spellContainer = i.stack() != null ? ISpellContainer.get(i.stack()) : null;
            if (spellContainer != null) {
               SpellSlot[] spells = spellContainer.getAllSpells();
               if (spells != null && !Arrays.stream(spells).toList().isEmpty()) {
                  int initialIndex = event.getManager().getSpellCount();

                  for (int spellIndex = initialIndex; spellIndex < initialIndex + spells.length; spellIndex++) {
                     SpellSlot spell = spells[spellIndex - initialIndex];
                     if (spell == null || spell.getSpell() == null) {
                        return;
                     }

                     event.addSelectionOption(new SpellData(spell.getSpell(), spell.getLevel(), true), i.stack().getItem().getDescriptionId(), spellIndex);
                  }
               }
            }
         }
      });
   }

   @SubscribeEvent
   public static void preventEffectRemoval(Remove event) {
      MobEffectInstance effectInstance = event.getEffectInstance();
      if (effectInstance != null && effectInstance.getEffect() == GSEffects.FROZEN_SIGHT.get()) {
         event.setCanceled(true);
      }

      if (effectInstance != null && effectInstance.getEffect() == GSEffects.CASTING.get()) {
         event.setCanceled(true);
      }

      if (effectInstance != null && effectInstance.getEffect() == GSEffects.AERIAL_COLLAPSE_HELPER.get()) {
         event.setCanceled(true);
      }

      if (effectInstance != null && effectInstance.getEffect() == GSEffects.THIRD_PERSON_SWITCH.get()) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onSpellCast(SpellOnCastEvent event) {
      LivingEntity caster = event.getEntity();
      if (!caster.isDeadOrDying()) {
         if (caster.hasEffect(GSEffects.HEMORRHAGE.get())) {
            float maxHp = caster.getMaxHealth();
            float hpToConsume = maxHp * 0.15F;
            caster.hurt(caster.damageSources().magic(), hpToConsume);
         }
      }
   }
}
