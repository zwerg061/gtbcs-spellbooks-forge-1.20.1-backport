package com.gametechbc.gametechbcsspellbooks.api.item.weapon;

import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.item.weapons.ExtendedWeaponTier;
import io.redspace.ironsspellbooks.item.weapons.IronsWeaponTier;
import java.util.function.Supplier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

public class GSItemTier implements Tier, IronsWeaponTier {
   public static ExtendedWeaponTier EXAMPLE_MAGIC_SWORD = new ExtendedWeaponTier(
      1500,
      7.5F,
      -2.5F,
      20,
      () -> Ingredient.of(new ItemLike[]{Items.DIAMOND}),
      new AttributeContainer[]{new AttributeContainer(() -> Attributes.ARMOR, 5.0, Operation.ADDITION)}
   );
   int uses;
   float damage;
   float speed;
   int enchantmentValue;
   TagKey<Block> incorrectBlocksForDrops;
   Supplier<Ingredient> repairIngredient;
   AttributeContainer[] attributes;

   public GSItemTier() {
   }

   public void ExtendedWeaponTier(
      int uses,
      float damage,
      float speed,
      int enchantmentValue,
      TagKey<Block> incorrectBlocksForDrops,
      Supplier<Ingredient> repairIngredient,
      AttributeContainer... attributes
   ) {
      this.uses = uses;
      this.damage = damage;
      this.speed = speed;
      this.enchantmentValue = enchantmentValue;
      this.incorrectBlocksForDrops = incorrectBlocksForDrops;
      this.repairIngredient = repairIngredient;
      this.attributes = attributes;
   }

   public int getLevel() {
      return 3;
   }

   public int getUses() {
      return this.uses;
   }

   public float getSpeed() {
      return this.speed;
   }

   public float getAttackDamageBonus() {
      return this.damage;
   }

   public TagKey<Block> getIncorrectBlocksForDrops() {
      return this.incorrectBlocksForDrops;
   }

   public int getEnchantmentValue() {
      return this.enchantmentValue;
   }

   public Ingredient getRepairIngredient() {
      return this.repairIngredient.get();
   }

   public AttributeContainer[] getAdditionalAttributes() {
      return this.attributes;
   }
}
