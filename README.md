# GTBC's Spellbooks — Forge 1.20.1 backport

Unofficial, community-made backport of **GameTechBC's Spellbooks** from NeoForge 1.21.1 to
**Minecraft Forge 1.20.1** (Forge 47.4.0, Java 17).

## Legal / credits

The original mod *GTBC's Spellbooks* and *GTBC's SpellLib* are **All Rights Reserved** by
GameTechBC. This repository contains decompiled and ported code derived from those mods and is
provided without permission from the original author. All credits remain with GameTechBC,
Iron (Iron's Spells 'n Spellbooks) and the respective dependency authors. **No rights are
granted by this repository.** If you are the rights holder and want this taken down, please
open an issue.

## Requirements

- Minecraft 1.20.1, Forge 47.4.0+
- Iron's Spells 'n Spellbooks 1.20.1-3.16.1
- GTBC's SpellLib 3.0.0 (Forge 1.20.1)
- irons_lib 1.20.1-1.0.2
- GeckoLib 4.8.3, Curios 5.14.1, PlayerAnimator 1.0.2

## Build

```bash
./gradlew build
./gradlew runData   # optional smoke test
```

The dependency jars needed for compilation and the dev runtime live in `libs/`.
`_tools/patch-all-refmaps.ps1` rewrites the SRG refmaps of those dependencies into official
names for the ForgeGradle dev workspace; the generated `*-runtime.jar` copies are committed so
`runClient`/`runData` work out of the box. The released mod jar is not affected.

Output: `build/libs/gametechbcs_spellbooks-3.0.0-1.20.1.jar`

See `BACKPORT_NOTES.md` for the full list of API migrations.
