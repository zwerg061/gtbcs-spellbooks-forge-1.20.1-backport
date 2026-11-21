# GTBC's Spellbooks — privater Forge-1.20.1-Backport

**Nur für den eigenen, privaten Spielgebrauch. All Rights Reserved. Nicht veröffentlichen, nicht pushen, nicht weitergeben.**

Quelle: `gametechbcs_spellbooks-3.0.0-1.21.1.jar` (NeoForge 1.21.1), dekompiliert mit Vineflower 1.11.1.

## Was gemacht wurde
- Forge 1.20.1 MDK (Forge 47.4.0, Java 17, Gradle 8.8, official mappings).
- Alle `net.neoforged.*` Imports/EventBus-Registrierungen auf `net.minecraftforge.*` umgestellt.
- 1.21-Datenkomponenten -> `ItemStack`-NBT (`CompoundTag`) in `DataComponentRegistry`, `CooldownManager`, `SwitchManager`, `SpiritPointsManager`.
- `Holder<Attribute>`/`Multimap<Holder<Attribute>,…>` -> Curios-5-API (`Attribute`, UUID-Argument).
- MobEffect-API 1.20.1 (`applyEffectTick` -> `void`, `MobEffect` statt `Holder<MobEffect>`, `String` statt `ResourceLocation` bei `addAttributeModifier`).
- NeoForge-Payload-Netzwerk entfernt (die Originalmod registrierte keine Nachrichten).
- Loot-Modifier auf Forge 1.20.1 `Codec`-API portiert, Loot-Tabellen-Zugriff über `LootDataManager`.
- `data/` auf 1.20.1-Pluralordner umgestellt (`recipes`, `loot_tables`, `tags/items`, `tags/entity_types`, `tags/mob_effects`, `forge/loot_modifiers`), `result.id` -> `result.item`.
- `META-INF/neoforge.mods.toml` -> `META-INF/mods.toml` (Forge-Format).
- Echtes `gtbcs_spell_lib-3.0.0-1.20.1.jar` (Forge) als Abhängigkeit eingebunden; `BloodEchoCurio` auf `AdvancedEchoCurio` umgestellt.

## Abhängigkeiten (in `libs/`)
- `irons_spellbooks-1.20.1-3.16.1.jar` (+ `irons_lib-1.20.1-1.0.2.jar`)
- `gtbcs_spell_lib-3.0.0-1.20.1.jar`
- `geckolib-forge-1.20.1-4.8.3.jar`
- `curios-forge-5.14.1+1.20.1.jar`
- `player-animation-lib-forge-1.0.2-rc1+1.20.jar`
- `jei-1.20.1-forge-15.20.0.112.jar`

## Build / Smoke-Test
```
./gradlew build
./gradlew runData
```
Verifiziert: `BUILD SUCCESSFUL`, keine ERROR-Zeilen. Ergebnis-JAR: `build/libs/gametechbcs_spellbooks-3.0.0-1.20.1.jar`.

Hinweis zum Dev-Workspace: ForgeGradle lässt die SRG-Namen in den Refmaps der Dependency-Jars stehen, während es die Klassen auf offizielle Namen umschreibt. Für `runClient`/`runData` werden deshalb lokal gepatchte Kopien (`libs/*-runtime.jar`, erzeugt mit `_tools/patch-all-refmaps.ps1`) benutzt. Das fertige Mod-JAR ist davon nicht betroffen.
