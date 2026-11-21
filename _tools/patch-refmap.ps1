$ErrorActionPreference = 'Stop'
Add-Type -AssemblyName System.IO.Compression
Add-Type -AssemblyName System.IO.Compression.FileSystem

$project = (Get-ChildItem -LiteralPath 'G:\Coode\Minecraft' -Directory | Where-Object { $_.Name -like 'mod*' } | Select-Object -First 1).FullName + '\magie'
$srcJar = 'C:\Users\jdune\.gradle\caches\forge_gradle\deobf_dependencies\blank\irons_spellbooks\1.20.1-3.16.1_mapped_official_1.20.1\irons_spellbooks-1.20.1-3.16.1_mapped_official_1.20.1.jar'
$dstJar = Join-Path $project 'libs\irons_spellbooks-1.20.1-3.16.1-runtime.jar'
$tsrg = Get-ChildItem -LiteralPath (Join-Path $project 'build\fg_cache\de\oceanlabs\mcp\mcp_config') -Recurse -File | Where-Object { $_.Name -like 'srg_to_official_1.20.1.tsrg' } | Select-Object -First 1

# SRG name -> official name
$map = @{}
foreach ($line in [System.IO.File]::ReadAllLines($tsrg.FullName)) {
    if ($line -match '^\s+(m_\d+_|f_\d+_)\s+.*?\s+(\S+)\s*$') {
        $map[$Matches[1]] = $Matches[2]
    }
}
"SRG mappings loaded: $($map.Count)"

$zip = [System.IO.Compression.ZipFile]::OpenRead($srcJar)
$refmapEntry = $zip.Entries | Where-Object { $_.Name -like '*refmap*.json' } | Select-Object -First 1
if (-not $refmapEntry) { throw 'refmap not found' }
$reader = New-Object System.IO.StreamReader($refmapEntry.Open())
$refmap = $reader.ReadToEnd()
$reader.Dispose()

$replaced = 0
$refmap = [regex]::Replace($refmap, '\b(m_\d+_|f_\d+_)\b', {
    param($m)
    if ($map.ContainsKey($m.Value)) { $script:replaced++; return $map[$m.Value] }
    return $m.Value
})
"SRG tokens replaced in refmap: $script:replaced"

if (Test-Path -LiteralPath $dstJar) { Remove-Item -LiteralPath $dstJar -Force }
$out = [System.IO.Compression.ZipFile]::Open($dstJar, [System.IO.Compression.ZipArchiveMode]::Create)
foreach ($entry in $zip.Entries) {
    $newEntry = $out.CreateEntry($entry.FullName, [System.IO.Compression.CompressionLevel]::Optimal)
    $dstStream = $newEntry.Open()
    if ($entry.FullName -eq $refmapEntry.FullName) {
        $bytes = [System.Text.Encoding]::UTF8.GetBytes($refmap)
        $dstStream.Write($bytes, 0, $bytes.Length)
    } else {
        $srcStream = $entry.Open()
        $srcStream.CopyTo($dstStream)
        $srcStream.Dispose()
    }
    $dstStream.Dispose()
}
$out.Dispose()
$zip.Dispose()
"Wrote $dstJar ($((Get-Item -LiteralPath $dstJar).Length) bytes)"
