package com.gametechbc.gametechbcsspellbooks.api.item.curio;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

/** 1.20.1 port: Curios 5 uses plain Attribute keys instead of Holder&lt;Attribute&gt;. */
public abstract class HeavyAttributeCurio extends CurioBaseItem {
    private final Multimap<Attribute, AttributeModifier> attributeMap = HashMultimap.create();

    public HeavyAttributeCurio(Properties properties, Map<Attribute, AttributeModifier> attributes) {
        super(properties);

        for (Entry<Attribute, AttributeModifier> entry : attributes.entrySet()) {
            this.attributeMap.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Builder<Attribute, AttributeModifier> attributeBuilder = ImmutableMultimap.builder();

        for (Attribute attribute : this.attributeMap.keySet()) {
            for (AttributeModifier modifier : this.attributeMap.get(attribute)) {
                AttributeModifier newModifier = new AttributeModifier(uuid, modifier.getName(), modifier.getAmount(), modifier.getOperation());
                attributeBuilder.put(attribute, newModifier);
            }
        }

        return attributeBuilder.build();
    }

    public abstract void appendHoverText(ItemStack var1, @Nullable Level var2, List<Component> var3, TooltipFlag var4);
}
