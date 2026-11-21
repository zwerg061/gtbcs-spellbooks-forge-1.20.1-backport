package com.gametechbc.gametechbcsspellbooks.loot;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/** 1.20.1 port: Forge global loot modifiers use Codec (not MapCodec) for their serializers. */
public class GSLootRegistry {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(
        ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, "gametechbcs_spellbooks"
    );
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> KEY_LOOT_MODIFIER = LOOT_MODIFIER_SERIALIZERS.register(
        "key_loot", () -> KeyLootModifier.CODEC.get()
    );

    public GSLootRegistry() {
    }

    public static void register(IEventBus eventBus) {
        LOOT_MODIFIER_SERIALIZERS.register(eventBus);
    }
}
