package com.gametechbc.gametechbcsspellbooks.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootDataId;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

/** 1.20.1 port of the chest loot modifier. */
public class KeyLootModifier extends LootModifier {
    public static final Supplier<Codec<KeyLootModifier>> CODEC = Suppliers.memoize(
        () -> RecordCodecBuilder.create(inst -> codecStart(inst)
            .and(Codec.STRING.fieldOf("key").forGetter(m -> m.resourceLocationKey))
            .apply(inst, KeyLootModifier::new))
    );
    private final String resourceLocationKey;

    protected KeyLootModifier(LootItemCondition[] conditionsIn, String resourceLocationKey) {
        super(conditionsIn);
        this.resourceLocationKey = resourceLocationKey;
    }

    @NotNull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        ResourceLocation path = new ResourceLocation(this.resourceLocationKey);
        LootTable lootTable = context.getLevel().getServer().getLootData().getElement(new LootDataId<>(LootDataType.TABLE, path));
        if (lootTable == null) return generatedLoot;
        ObjectArrayList<ItemStack> extraLoot = new ObjectArrayList<>();
        lootTable.getRandomItemsRaw(context, extraLoot::add);
        generatedLoot.addAll(extraLoot);
        return generatedLoot;
    }

    @Override
    public Codec<KeyLootModifier> codec() {
        return CODEC.get();
    }
}
