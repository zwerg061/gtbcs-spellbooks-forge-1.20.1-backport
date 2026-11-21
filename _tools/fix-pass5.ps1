$ErrorActionPreference = 'Stop'
$project = (Get-ChildItem -LiteralPath 'G:\Coode\Minecraft' -Directory | Where-Object { $_.Name -like 'mod*' } | Select-Object -First 1).FullName + '\magie'
$root = Join-Path $project 'src\main\java\com\gametechbc\gametechbcsspellbooks'

$fixes = @(
    @('effects\FlamesReborn\FlamesRebornHandler.java', @('import net.minecraftforge.fml.common.EventBusSubscriber;', 'import net.minecraftforge.fml.common.Mod.EventBusSubscriber;')),
    @('Config.java', @('((List)ITEM_STRINGS.get())', '((List<String>)ITEM_STRINGS.get())')),
    @('effects\LingeringStrain\LingeringStrainHandler.java', @(
        'onServerTick(LivingDamageEvent event)', 'onServerTick(ServerTickEvent event)',
        'ServerLevel entityLevel = entity.level().getServer().getLevel(damage.dimension);', 'ServerLevel entityLevel = event.getServer().getLevel(damage.dimension);'
    )),
    @('entity\extended_mobs\ExtendedDeadKingBoss.java', @('AttributeRegistry.SPELL_RESIST', 'AttributeRegistry.SPELL_RESIST.get()')),
    @('entity\spells\reversal\ReversalRenderer.java', @('.setColor(', '.color(')),
    @('spells\blood\CallForthTheDeadKingSpell.java', @(
        'MobSpawnType.MOB_SUMMONED, null);', 'MobSpawnType.MOB_SUMMONED, null, null);'
    )),
    @('effects\Reversal\ReversalEffectInstance.java', @(
        'RegistryObject<MobEffect> effect,', 'MobEffect effect,',
        'super(GSEffects.REVERSAL.get(), duration', 'super(effect, duration'
    ))
)

foreach ($entry in $fixes) {
    $rel = $entry[0]; $pairs = $entry[1]
    $f = Join-Path $root $rel
    if (-not (Test-Path -LiteralPath $f)) { "MISSING: $rel"; continue }
    $t = [System.IO.File]::ReadAllText($f); $o = $t
    for ($i = 0; $i -lt $pairs.Count; $i += 2) { $t = $t.Replace($pairs[$i], $pairs[$i + 1]) }
    if ($t -ne $o) { [System.IO.File]::WriteAllText($f, $t, (New-Object System.Text.UTF8Encoding($false))); "patched $rel" }
    else { "NO CHANGE: $rel" }
}

# MobEffect lists in 1.20.1: getEffect() returns MobEffect, not Holder.
foreach ($rel in @('effects\Reversal\ReversalEffect.java','entity\spells\AcidRainAoe.java','spells\holy\BanishSpell.java')) {
    $f = Join-Path $root $rel
    $t = [System.IO.File]::ReadAllText($f); $o = $t
    $t = $t.Replace('holder -> ((MobEffect)holder.value()).getCategory()', 'effect -> effect.getCategory()')
    $t = $t.Replace('!holder.is(GSTags.BANISH_IMMUNE)', '!net.minecraft.core.registries.BuiltInRegistries.MOB_EFFECT.wrapAsHolder(holder).is(GSTags.BANISH_IMMUNE)')
    if ($t -ne $o) { [System.IO.File]::WriteAllText($f, $t, (New-Object System.Text.UTF8Encoding($false))); "patched holder2 $rel" }
}
