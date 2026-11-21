package com.gametechbc.gametechbcsspellbooks.entity.extended_mobs;

import com.gametechbc.gametechbcsspellbooks.init.GSEntities;
import com.gametechbc.gametechbcsspellbooks.init.GSSpells;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.goals.DeadKingAnimatedWarlockAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingBoss;
import io.redspace.ironsspellbooks.entity.mobs.goals.PatrolNearLocationGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.SpellBarrageGoal;
import java.util.List;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ExtendedDeadKingBoss extends DeadKingBoss {
   public ExtendedDeadKingBoss(EntityType<? extends DeadKingBoss> pEntityType, Level pLevel) {
      super(pEntityType, pLevel);
      this.setPersistenceRequired();
      this.xpReward = 60;
   }

   public ExtendedDeadKingBoss(Level level) {
      this((EntityType<? extends DeadKingBoss>)GSEntities.EXTENDED_DEAD_KING.get(), level);
      this.setPersistenceRequired();
   }

   protected void setFinalPhaseGoals() {
      this.goalSelector.getAvailableGoals().forEach(WrappedGoal::stop);
      this.goalSelector.removeAllGoals(x -> true);
      this.goalSelector
         .addGoal(1, new ExtendedDeadKingBoss.ExtendedDeadKingBarrageGoal(this, (AbstractSpell)SpellRegistry.WITHER_SKULL_SPELL.get(), 5, 5, 60, 140, 4));
      this.goalSelector
         .addGoal(2, new ExtendedDeadKingBoss.ExtendedDeadKingBarrageGoal(this, (AbstractSpell)SpellRegistry.SUMMON_VEX_SPELL.get(), 3, 5, 400, 600, 1));
      this.goalSelector.addGoal(3, new ExtendedDeadKingBoss.ExtendedDeadKingBarrageGoal(this, GSSpells.AERIAL_COLLAPSE_SPELL.get(), 4, 5, 240, 400, 1));
      this.goalSelector
         .addGoal(
            4,
            new DeadKingAnimatedWarlockAttackGoal(this, 1.0, 55, 85)
               .setSpellQuality(0.6F, 0.8F)
               .setSpells(
                  List.of(
                     (AbstractSpell)SpellRegistry.BLOOD_SLASH_SPELL.get(),
                     (AbstractSpell)SpellRegistry.WITHER_SKULL_SPELL.get(),
                     (AbstractSpell)SpellRegistry.WITHER_SKULL_SPELL.get(),
                     GSSpells.ASHEN_BREATH.get(),
                     (AbstractSpell)SpellRegistry.BLAZE_STORM_SPELL.get(),
                     (AbstractSpell)SpellRegistry.ACID_ORB_SPELL.get()
                  ),
                  List.of(GSSpells.METEOR_STORM_SPELL.get()),
                  List.of((AbstractSpell)SpellRegistry.BLOOD_STEP_SPELL.get()),
                  List.of()
               )
               .setIsFlying()
               .setSingleUseSpell(GSSpells.BLACKOUT_SPELL.get(), 10, 60, 3, 3)
               .setMeleeBias(0.8F, 0.8F)
               .setAllowFleeing(false)
         );
      this.goalSelector.addGoal(5, new PatrolNearLocationGoal(this, 32.0F, 0.9F));
      this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.hasUsedSingleAttack = false;
   }

   public static Builder deadkingAttributes() {
      return LivingEntity.createLivingAttributes()
         .add(Attributes.ATTACK_DAMAGE, 12.0)
         .add(AttributeRegistry.SPELL_POWER.get(), 1.5)
         .add(Attributes.ARMOR, 20.0)
         .add(AttributeRegistry.SPELL_RESIST.get(), 1.25)
         .add(Attributes.MAX_HEALTH, 600.0)
         .add(Attributes.KNOCKBACK_RESISTANCE, 0.8)
         .add(Attributes.ATTACK_KNOCKBACK, 0.6)
         .add(net.minecraftforge.common.ForgeMod.ENTITY_REACH.get(), 4.0)
         .add(Attributes.FOLLOW_RANGE, 64.0)
         .add(Attributes.MOVEMENT_SPEED, 0.155);
   }

   public boolean hurt(DamageSource source, float damage) {
      if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
         return super.hurt(source, damage);
      } else {
         damage = Math.min(this.DamageCap(), damage);
         return super.hurt(source, damage);
      }
   }

   public float DamageCap() {
      return 20.0F;
   }

   private class ExtendedDeadKingBarrageGoal extends SpellBarrageGoal {
      public ExtendedDeadKingBarrageGoal(
         IMagicEntity abstractSpellCastingMob,
         AbstractSpell spell,
         int minLevel,
         int maxLevel,
         int pAttackIntervalMin,
         int pAttackIntervalMax,
         int projectileCount
      ) {
         super(abstractSpellCastingMob, spell, minLevel, maxLevel, pAttackIntervalMin, pAttackIntervalMax, projectileCount);
      }

      public boolean canUse() {
         return !ExtendedDeadKingBoss.this.isMeleeing && super.canUse();
      }
   }
}
