$ErrorActionPreference = 'Stop'
Add-Type -AssemblyName System.IO.Compression
Add-Type -AssemblyName System.IO.Compression.FileSystem

$project = (Get-ChildItem -LiteralPath 'G:\Coode\Minecraft' -Directory | Where-Object { $_.Name -like 'mod*' } | Select-Object -First 1).FullName + '\magie'
$cacheRoot = 'C:\Users\jdune\.gradle\caches\forge_gradle\deobf_dependencies\blank'
$tsrg = Get-ChildItem -LiteralPath (Join-Path $project 'build\fg_cache\de\oceanlabs\mcp\mcp_config') -Recurse -File | Where-Object { $_.Name -like 'srg_to_official_1.20.1.tsrg' } | Select-Object -First 1

$map = @{}
foreach ($line in [System.IO.File]::ReadAllLines($tsrg.FullName)) {
    if ($line -match '^\s+(m_\d+_|f_\d+_)\s+\S+\s+(\S+)\s*$') {
        # method: srg name, descriptor, official name
        $map[$Matches[1]] = $Matches[2]
    } elseif ($line -match '^\s+(f_\d+_)\s+(\S+)\s*$') {
        # field: srg name, official name (no descriptor)
        $map[$Matches[1]] = $Matches[2]
    }
}

# All deobfuscated runtime mod jars that have a refmap
$jars = Get-ChildItem -LiteralPath $cacheRoot -Recurse -File -Filter '*.jar' |
    Where-Object { $_.FullName -notmatch '\.input$' }

foreach ($jar in $jars) {
    $outName = '{0}-runtime.jar' -f [System.IO.Path]::GetFileNameWithoutExtension($jar.Name)
    $dst = Join-Path $project "libs\$outName"
    $zip = [System.IO.Compression.ZipFile]::OpenRead($jar.FullName)
    $refEntries = @($zip.Entries | Where-Object { $_.Name -like '*refmap*.json' })
    if ($refEntries.Count -eq 0) { $zip.Dispose(); continue }

    $patched = @{}
    foreach ($entry in $refEntries) {
        $reader = New-Object System.IO.StreamReader($entry.Open())
        $text = $reader.ReadToEnd(); $reader.Dispose()
        $n = 0
        $text = [regex]::Replace($text, '\b(m_\d+_|f_\d+_)\b', {
            param($m) if ($map.ContainsKey($m.Value)) { $script:n++; return $map[$m.Value] } else { return $m.Value }
        })
        $patched[$entry.FullName] = [System.Text.Encoding]::UTF8.GetBytes($text)
        "$($jar.Name): $($entry.Name) -> $script:n tokens"
    }

    if (Test-Path -LiteralPath $dst) { Remove-Item -LiteralPath $dst -Force }
    $out = [System.IO.Compression.ZipFile]::Open($dst, [System.IO.Compression.ZipArchiveMode]::Create)
    foreach ($entry in $zip.Entries) {
        $newEntry = $out.CreateEntry($entry.FullName, [System.IO.Compression.CompressionLevel]::Optimal)
        $dstStream = $newEntry.Open()
        if ($patched.ContainsKey($entry.FullName)) {
            $b = $patched[$entry.FullName]
            $dstStream.Write($b, 0, $b.Length)
        } else {
            $src = $entry.Open(); $src.CopyTo($dstStream); $src.Dispose()
        }
        $dstStream.Dispose()
    }
    $out.Dispose(); $zip.Dispose()
    "Wrote $outName"
}
