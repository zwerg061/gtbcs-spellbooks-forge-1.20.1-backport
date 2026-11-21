package com.gametechbc.gametechbcsspellbooks.data_manager;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

/** 1.20.1 port: cooldowns live in the item's CompoundTag instead of a data component. */
public class CooldownManager {
    private static final String COOLDOWN_TAG = "Cooldowns";

    public CooldownManager() {
    }

    public static int getCooldown(ItemStack stack, String abilityKey) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(COOLDOWN_TAG, CompoundTag.TAG_COMPOUND)) {
            return tag.getCompound(COOLDOWN_TAG).getInt(abilityKey);
        }
        return 0;
    }

    public static void setCooldown(ItemStack stack, String abilityKey, int cooldown, int maxCooldown) {
        CompoundTag root = stack.getOrCreateTag();
        CompoundTag cooldowns = root.getCompound(COOLDOWN_TAG);
        cooldowns.putInt(abilityKey, Math.min(cooldown, maxCooldown));
        root.put(COOLDOWN_TAG, cooldowns);
    }

    public static void addCooldown(ItemStack stack, String abilityKey, int amount, int maxCooldown) {
        int newCooldown = Math.min(getCooldown(stack, abilityKey) + amount, maxCooldown);
        setCooldown(stack, abilityKey, newCooldown, maxCooldown);
    }

    public static void reduceCooldown(ItemStack stack, String abilityKey, int amount) {
        int newCooldown = Math.max(getCooldown(stack, abilityKey) - amount, 0);
        setCooldown(stack, abilityKey, newCooldown, Integer.MAX_VALUE);
    }

    public static boolean isCooldownActive(ItemStack stack, String abilityKey) {
        return getCooldown(stack, abilityKey) > 0;
    }

    public static void tickCooldown(ItemStack stack) {
        CompoundTag root = stack.getTag();
        if (root != null && root.contains(COOLDOWN_TAG, CompoundTag.TAG_COMPOUND)) {
            CompoundTag cooldowns = root.getCompound(COOLDOWN_TAG);
            for (String key : cooldowns.getAllKeys()) {
                cooldowns.putInt(key, Math.max(cooldowns.getInt(key) - 1, 0));
            }
            root.put(COOLDOWN_TAG, cooldowns);
        }
    }
}
