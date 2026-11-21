package com.gametechbc.gametechbcsspellbooks.item.weapon;

import com.gametechbc.gametechbcsspellbooks.api.SpiritPointsManager;
import com.gametechbc.gametechbcsspellbooks.api.item.weapon.GSItemTier;
import com.gametechbc.gametechbcsspellbooks.data_manager.CooldownManager;
import com.gametechbc.gametechbcsspellbooks.data_manager.SwitchManager;
import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class GSExampleWeaponItem extends MagicSwordItem {
   public GSExampleWeaponItem() {
      super(
         GSItemTier.EXAMPLE_MAGIC_SWORD,
         ItemPropertiesHelper.equipment().rarity(Rarity.EPIC),
         SpellDataRegistryHolder.of(new SpellDataRegistryHolder[]{new SpellDataRegistryHolder(SpellRegistry.COUNTERSPELL_SPELL, 1)})
      );
   }

   public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
      if (attacker instanceof Player player) {
         SpiritPointsManager.addSpiritPoints(stack, player, 20);
         if (player.isCrouching()) {
            if (!SwitchManager.isEnabled(stack)) {
               SwitchManager.toggleSwitch(stack);
            }

            CooldownManager.setCooldown(stack, "hurt_crouch", 200, 200);
         } else {
            if (SwitchManager.isEnabled(stack)) {
               SwitchManager.toggleSwitch(stack);
            }

            CooldownManager.setCooldown(stack, "hurt", 200, 200);
         }
      }

      return super.hurtEnemy(stack, target, attacker);
   }

   public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
      if (!level.isClientSide && entity instanceof Player) {
         CooldownManager.tickCooldown(stack);
      }

      super.inventoryTick(stack, level, entity, slot, selected);
   }
}
