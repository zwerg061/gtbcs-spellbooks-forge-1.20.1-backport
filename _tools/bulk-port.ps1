$ErrorActionPreference = 'Stop'
$project = (Get-ChildItem -LiteralPath 'G:\Coode\Minecraft' -Directory | Where-Object { $_.Name -like 'mod*' } | Select-Object -First 1).FullName + '\magie'
$root = Join-Path $project 'src\main\java'

function Replace-InText([string]$text, [string]$pattern, [string]$replacement) {
    return [regex]::Replace($text, $pattern, $replacement)
}

$count = 0
foreach ($f in (Get-ChildItem -LiteralPath $root -Recurse -Filter '*.java')) {
    $t = [System.IO.File]::ReadAllText($f.FullName)
    $o = $t

    # 1.20.1 enum/API names
    $t = $t.Replace('ADD_MULTIPLIED_BASE', 'MULTIPLY_BASE')
    $t = $t.Replace('ADD_MULTIPLIED_TOTAL', 'MULTIPLY_TOTAL')
    $t = $t.Replace('Operation.ADD_VALUE', 'Operation.ADDITION')
    $t = $t.Replace('.id()', '.getId()')
    $t = $t.Replace('.amount()', '.getAmount()')
    $t = $t.Replace('.operation()', '.getOperation()')
    $t = $t.Replace('LootItemFunctionType<?>', 'LootItemFunctionType')
    $t = $t.Replace('BlockTags.INCORRECT_FOR_DIAMOND_TOOL', 'BlockTags.NEEDS_DIAMOND_TOOL')
    $t = $t.Replace('net.minecraftforge.client.event.EntityViewRenderEvent.FogColors', 'net.minecraftforge.client.event.ViewportEvent.ComputeFogColor')

    # Forge 1.20.1 MobEffect.applyEffectTick returns void and takes MobEffect (RegistryObject -> .get())
    if ($f.Name -ne 'GSEffects.java') {
        $t = Replace-InText $t '\bGSEffects\.([A-Z_0-9]+)\b(?!\s*\.)' 'GSEffects.$1.get()'
        $t = Replace-InText $t '\bMobEffectRegistry\.([A-Z_0-9]+)\b(?!\s*\.)' 'MobEffectRegistry.$1.get()'
        $t = Replace-InText $t '\bSoundRegistry\.([A-Z_0-9]+)\b(?!\s*\.)' 'SoundRegistry.$1.get()'
    }

    # applyEffectTick: boolean -> void; return <expr> -> return; inside that method only
    $t = $t.Replace('public boolean applyEffectTick(', 'public void applyEffectTick(')
    $idx = 0
    while (($pos = $t.IndexOf('public void applyEffectTick(', $idx)) -ge 0) {
        $brace = $t.IndexOf('{', $pos)
        if ($brace -lt 0) { break }
        $depth = 0; $end = -1
        for ($i = $brace; $i -lt $t.Length; $i++) {
            if ($t[$i] -eq '{') { $depth++ }
            elseif ($t[$i] -eq '}') { $depth--; if ($depth -eq 0) { $end = $i; break } }
        }
        if ($end -lt 0) { break }
        $body = $t.Substring($brace, $end - $brace + 1)
        $body = Replace-InText $body '\breturn\s+[^;{}]+;' 'return;'
        $t = $t.Substring(0, $brace) + $body + $t.Substring($end + 1)
        $idx = $brace + $body.Length
    }

    # NeoForge EffectCure has no Forge 1.20.1 counterpart: drop the override method + import.
    $t = $t.Replace('import net.minecraftforge.common.effect.EffectCure;', '')
    $t = Replace-InText $t '(?m)^\s*public void fillEffectCures\(Set<EffectCure> cures, MobEffectInstance effectInstance\) \{\r?\n\s*\}\r?\n' ''

    if ($t -ne $o) {
        [System.IO.File]::WriteAllText($f.FullName, $t, (New-Object System.Text.UTF8Encoding($false)))
        $count++
    }
}
"Changed $count files"
