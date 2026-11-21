package com.gametechbc.gametechbcsspellbooks.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

/**
 * 1.20.1 compatibility shim.
 *
 * The 1.21.1 original stored the mod's custom per-item data in DataComponents.
 * Minecraft 1.20.1 has no data components, so everything is stored in the
 * ItemStack's CompoundTag instead. The string constants below are the NBT keys
 * that replace the former component types.
 */
public final class DataComponentRegistry {
    public static final String CUSTOM_COMPONENT = "gametechbcs_custom";
    public static final String ECHO_CURIO_COMPONENT = "gametechbcs_echo_curio";
    public static final String SWITCH_COMPONENT = "gametechbcs_switch";

    private DataComponentRegistry() {
    }

    public static boolean has(ItemStack stack, String key) {
        return stack.getTag() != null && stack.getTag().contains(key, CompoundTag.TAG_COMPOUND);
    }

    public static CompoundTag get(ItemStack stack, String key) {
        CompoundTag root = stack.getOrCreateTag();
        if (!root.contains(key, CompoundTag.TAG_COMPOUND)) {
            root.put(key, new CompoundTag());
        }
        return root.getCompound(key);
    }

    public static void set(ItemStack stack, String key, CompoundTag tag) {
        stack.getOrCreateTag().put(key, tag.copy());
    }
}
