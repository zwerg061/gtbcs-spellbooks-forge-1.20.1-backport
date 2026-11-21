$ErrorActionPreference = 'Stop'
$project = (Get-ChildItem -LiteralPath 'G:\Coode\Minecraft' -Directory | Where-Object { $_.Name -like 'mod*' } | Select-Object -First 1).FullName + '\magie'
$root = Join-Path $project 'src\main\java'

# Ordered: longest / most specific patterns first.
$map = [ordered]@{
    'net.neoforged.api.distmarker'                                      = 'net.minecraftforge.api.distmarker'
    'net.neoforged.bus.api.IEventBus'                                   = 'net.minecraftforge.eventbus.api.IEventBus'
    'net.neoforged.bus.api.SubscribeEvent'                              = 'net.minecraftforge.eventbus.api.SubscribeEvent'
    'net.neoforged.fml.common.EventBusSubscriber.Bus'                   = 'net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus'
    'net.neoforged.fml.common.EventBusSubscriber'                       = 'net.minecraftforge.fml.common.Mod.EventBusSubscriber'
    'net.neoforged.fml.common.Mod'                                      = 'net.minecraftforge.fml.common.Mod'
    'net.neoforged.fml.event.lifecycle'                                 = 'net.minecraftforge.fml.event.lifecycle'
    'net.neoforged.fml.event.config.ModConfigEvent'                     = 'net.minecraftforge.fml.event.config.ModConfigEvent'
    'net.neoforged.fml.config.ModConfig'                                = 'net.minecraftforge.fml.config.ModConfig'
    'net.neoforged.fml.loading.FMLPaths'                                = 'net.minecraftforge.fml.loading.FMLPaths'
    'net.neoforged.fml.loading.FMLEnvironment'                          = 'net.minecraftforge.fml.loading.FMLEnvironment'
    'net.neoforged.fml.util.thread.SidedThreadGroups'                   = 'net.minecraftforge.fml.util.thread.SidedThreadGroups'
    'net.neoforged.fml.ModContainer'                                    = 'net.minecraftforge.fml.ModContainer'
    'net.neoforged.neoforge.common.NeoForge'                            = 'net.minecraftforge.common.MinecraftForge'
    'net.neoforged.neoforge.common.ModConfigSpec'                       = 'net.minecraftforge.common.ForgeConfigSpec'
    'net.neoforged.neoforge.registries.DeferredHolder'                  = 'net.minecraftforge.registries.RegistryObject'
    'net.neoforged.neoforge.registries.DeferredRegister'                = 'net.minecraftforge.registries.DeferredRegister'
    'net.neoforged.neoforge.registries.NeoForgeRegistries.Keys'         = 'net.minecraftforge.registries.ForgeRegistries.Keys'
    'net.neoforged.neoforge.common.loot.IGlobalLootModifier'            = 'net.minecraftforge.common.loot.IGlobalLootModifier'
    'net.neoforged.neoforge.common.loot.LootModifier'                   = 'net.minecraftforge.common.loot.LootModifier'
    'net.neoforged.neoforge.entity.IEntityWithComplexSpawn'             = 'net.minecraftforge.entity.IEntityAdditionalSpawnData'
    'net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent'  = 'net.minecraftforge.event.entity.EntityAttributeCreationEvent'
    'net.neoforged.neoforge.event.entity.living.LivingDamageEvent.Post' = 'net.minecraftforge.event.entity.living.LivingDamageEvent'
    'net.neoforged.neoforge.event.entity.living.LivingDamageEvent.Pre'  = 'net.minecraftforge.event.entity.living.LivingDamageEvent'
    'net.neoforged.neoforge.event.entity.living.LivingDeathEvent'       = 'net.minecraftforge.event.entity.living.LivingDeathEvent'
    'net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent'   = 'net.minecraftforge.event.entity.living.LivingKnockBackEvent'
    'net.neoforged.neoforge.event.entity.living.MobEffectEvent.Remove'  = 'net.minecraftforge.event.entity.living.MobEffectEvent.Remove'
    'net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent' = 'net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent'
    'net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickBlock' = 'net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock'
    'net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickItem'  = 'net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem'
    'net.neoforged.neoforge.event.tick.PlayerTickEvent.Pre'             = 'net.minecraftforge.event.entity.player.PlayerTickEvent'
    'net.neoforged.neoforge.event.tick.ServerTickEvent.Post'            = 'net.minecraftforge.event.TickEvent.ServerTickEvent'
    'net.neoforged.neoforge.client.event.ComputeFovModifierEvent'       = 'net.minecraftforge.client.event.ComputeFovModifierEvent'
    'net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions' = 'net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions'
    'net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers' = 'net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers'
    'net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent' = 'net.minecraftforge.client.event.RegisterParticleProvidersEvent'
    'net.neoforged.neoforge.client.event.RenderLevelStageEvent'         = 'net.minecraftforge.client.event.RenderLevelStageEvent'
    'net.neoforged.neoforge.client.event.ViewportEvent.ComputeFogColor' = 'net.minecraftforge.client.event.EntityViewRenderEvent.FogColors'
    'net.neoforged.neoforge.common.EffectCure'                          = 'net.minecraftforge.common.effect.EffectCure'
}

$files = Get-ChildItem -LiteralPath $root -Recurse -Filter '*.java'
$changed = 0
foreach ($f in $files) {
    $text = [System.IO.File]::ReadAllText($f.FullName)
    $orig = $text
    foreach ($k in $map.Keys) {
        $text = $text.Replace($k, $map[$k])
    }
    $text = $text.Replace('NeoForge.EVENT_BUS', 'MinecraftForge.EVENT_BUS')
    $text = $text.Replace('ModConfigSpec', 'ForgeConfigSpec')
    if ($text -ne $orig) {
        [System.IO.File]::WriteAllText($f.FullName, $text, (New-Object System.Text.UTF8Encoding($false)))
        $changed++
    }
}
"Changed $changed of $($files.Count) files"
