package com.gametechbc.gametechbcsspellbooks.setup;

import com.gametechbc.gametechbcsspellbooks.GametechbcsSpellbooksMod;
import com.gametechbc.gametechbcsspellbooks.effects.AstralSenseEffect;
import com.gametechbc.gametechbcsspellbooks.effects.AstralSenseTreasureEffect;
import com.gametechbc.gametechbcsspellbooks.effects.HemorrhageEffect;
import java.lang.reflect.Field;
import java.util.List;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(
   modid = "gametechbcs_spellbooks",
   bus = Bus.MOD,
   value = {Dist.CLIENT}
)
public class ModClientEvents {
    public ModClientEvents() {
    }

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent e) {
        e.enqueueWork(() -> {
            // MOB_EFFECT_FOG is private in 1.20.1; the inner FogFunction classes are made
            // public via META-INF/accesstransformer.cfg, but the list itself is not.
            try {
                Field field = FogRenderer.class.getDeclaredField("MOB_EFFECT_FOG");
                field.setAccessible(true);
                List<FogRenderer.MobEffectFogFunction> fogFunctions = (List<FogRenderer.MobEffectFogFunction>) field.get(null);
                fogFunctions.add(new AstralSenseEffect.AstralSenseFogFunction());
                fogFunctions.add(new AstralSenseTreasureEffect.AstralSenseFogFunction());
                fogFunctions.add(new HemorrhageEffect.HemorrhageFogFunction());
            } catch (ReflectiveOperationException | RuntimeException ex) {
                GametechbcsSpellbooksMod.LOGGER.error("Failed to register custom mob-effect fog functions", ex);
            }
        });
    }
}
