package com.gametechbc.gametechbcsspellbooks;

import com.gametechbc.gametechbcsspellbooks.effects.Assassin.AttackHandler;
import com.gametechbc.gametechbcsspellbooks.init.GSCreativeTabs;
import com.gametechbc.gametechbcsspellbooks.init.GSEffects;
import com.gametechbc.gametechbcsspellbooks.init.GSEntities;
import com.gametechbc.gametechbcsspellbooks.init.GSItems;
import com.gametechbc.gametechbcsspellbooks.init.GSParticles;
import com.gametechbc.gametechbcsspellbooks.init.GSSounds;
import com.gametechbc.gametechbcsspellbooks.init.GSSpells;
import com.gametechbc.gametechbcsspellbooks.loot.GSLootRegistry;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.render.SpellBookCurioRenderer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mod(GametechbcsSpellbooksMod.MODID)
public class GametechbcsSpellbooksMod {
    public static final Logger LOGGER = LogManager.getLogger(GametechbcsSpellbooksMod.class);
    public static final String MODID = "gametechbcs_spellbooks";
    private static final List<Tuple<Runnable, Integer>> workQueue = new ArrayList<>();

    public GametechbcsSpellbooksMod() {
        IEventBus modEventBus = net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        GSSounds.register(modEventBus);
        GSEffects.register(modEventBus);
        GSSpells.register(modEventBus);
        GSEntities.register(modEventBus);
        GSLootRegistry.register(modEventBus);
        GSItems.register(modEventBus);
        GSCreativeTabs.register(modEventBus);
        GSParticles.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(AttackHandler.class);

        Path configPath = FMLPaths.CONFIGDIR.get().resolve(MODID);
        try {
            Files.createDirectories(configPath);
        } catch (Exception e) {
            LOGGER.error("Failed to create config directory: " + configPath, e);
        }
        net.minecraftforge.fml.ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        // (Forge 1.20.1 has no constructor injection for @Mod classes.)
    }

    /** Schedules work to be executed on the server thread after the given number of ticks. */
    public static void queueServerWork(int tick, Runnable action) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
            workQueue.add(new Tuple<>(action, tick));
        }
    }

    @SubscribeEvent
    public void tick(ServerTickEvent event) {
        if (workQueue.isEmpty()) return;
        List<Tuple<Runnable, Integer>> actions = new ArrayList<>();
        for (Tuple<Runnable, Integer> work : workQueue) {
            work.setB(work.getB() - 1);
            if (work.getB() == 0) actions.add(work);
        }
        actions.forEach(e -> e.getA().run());
        workQueue.removeAll(actions);
    }

    public static ResourceLocation id(@NotNull String path) {
        return new ResourceLocation(MODID, path);
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> GSItems.getItems()
                .stream()
                .filter(item -> item.get() instanceof SpellBook)
                .forEach(item -> CuriosRendererRegistry.register((Item) item.get(), SpellBookCurioRenderer::new)));
        }
    }
}
