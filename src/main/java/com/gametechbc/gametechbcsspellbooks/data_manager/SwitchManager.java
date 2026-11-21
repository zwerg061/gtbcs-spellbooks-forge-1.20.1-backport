package com.gametechbc.gametechbcsspellbooks.data_manager;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

/** 1.20.1 port: switch state lives in the item's CompoundTag instead of a data component. */
public class SwitchManager {
    private static final String SWITCH_STATE_KEY = "SwitchState";

    public SwitchManager() {
    }

    public static boolean isEnabled(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.getBoolean(SWITCH_STATE_KEY);
    }

    public static void toggleSwitch(ItemStack stack) {
        stack.getOrCreateTag().putBoolean(SWITCH_STATE_KEY, !isEnabled(stack));
    }
}
