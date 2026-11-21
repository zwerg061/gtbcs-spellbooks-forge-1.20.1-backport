$ErrorActionPreference = 'Stop'
$project = (Get-ChildItem -LiteralPath 'G:\Coode\Minecraft' -Directory | Where-Object { $_.Name -like 'mod*' } | Select-Object -First 1).FullName + '\magie'
$root = Join-Path $project 'src\main\java\com\gametechbc\gametechbcsspellbooks'

$fixes = @(
    @('effects\FlamesReborn\FlamesRebornHandler.java', @('net.neoforged.fml.common', 'net.minecraftforge.fml.common')),
    @('Config.java', @('new ResourceLocation(itemName)', 'new ResourceLocation((String)itemName)')),
    @('spells\holy\NullflareSpell.java', @('AttributeRegistry.FIRE_SPELL_POWER', 'AttributeRegistry.FIRE_SPELL_POWER.get()', 'AttributeRegistry.ICE_SPELL_POWER', 'AttributeRegistry.ICE_SPELL_POWER.get()')),
    @('effects\CorrodedEffect.java', @('AttributeRegistry.FIRE_MAGIC_RESIST', 'AttributeRegistry.FIRE_MAGIC_RESIST.get()', 'FIRE_MAGIC_DOWN_ID,', 'FIRE_MAGIC_DOWN_ID.toString(),', 'ARMOR_DOWN_ID,', 'ARMOR_DOWN_ID.toString(),')),
    @('effects\HemorrhageEffect.java', @('AttributeRegistry.HOLY_MAGIC_RESIST', 'AttributeRegistry.HOLY_MAGIC_RESIST.get()', 'AttributeRegistry.SPELL_POWER', 'AttributeRegistry.SPELL_POWER.get()')),
    @('entity\extended_mobs\ExtendedDeadKingBoss.java', @('AttributeRegistry.SPELL_POWER', 'AttributeRegistry.SPELL_POWER.get()', 'Attributes.ENTITY_INTERACTION_RANGE', 'net.minecraftforge.common.ForgeMod.ENTITY_REACH.get()')),
    @('entity\spells\AcidRainAoe.java', @('entity.igniteForSeconds(entity.getRemainingFireTicks() / 20.0F + 1.0F)', 'entity.setSecondsOnFire((int)Math.ceil(entity.getRemainingFireTicks() / 20.0F + 1.0F))')),
    @('entity\spells\reversal\ReversalRenderer.java', @('.addVertex(', '.vertex(')),
    @('entity\spells\psychic_bolt\PsychicBoltProjectile.java', @('public Optional<Holder<SoundEvent>> getImpactSound()', 'public Optional<java.util.function.Supplier<SoundEvent>> getImpactSound()')),
    @('GametechbcsSpellbooksMod.java', @('modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC)', 'net.minecraftforge.fml.ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC)')),
    @('init\GSItems.java', @('Collection<RegistryObject<? extends Item>> getItems()', 'Collection<RegistryObject<Item>> getItems()')),
    @('item\imbued_echos\BloodEchoCurio.java', @('new AttributeModifier(new ResourceLocation("traveloptics", "blood_spell_power"), 0.15', 'new AttributeModifier("traveloptics:blood_spell_power", 0.15')),
    @('item\weapon\GSExampleWeaponItem.java', @('ItemPropertiesHelper.equipment().rarity(Rarity.EPIC).attributes(ExtendedSwordItem.createAttributes(GSItemTier.EXAMPLE_MAGIC_SWORD))', 'ItemPropertiesHelper.equipment().rarity(Rarity.EPIC)')),
    @('effects\LingeringStrain\LingeringStrainHandler.java', @('event.getServer().getLevel(damage.dimension)', 'entity.level().getServer().getLevel(damage.dimension)'))
)

foreach ($entry in $fixes) {
    $rel = $entry[0]
    $pairs = $entry[1]
    $f = Join-Path $root $rel
    if (-not (Test-Path -LiteralPath $f)) { "MISSING: $rel"; continue }
    $t = [System.IO.File]::ReadAllText($f)
    $o = $t
    for ($i = 0; $i -lt $pairs.Count; $i += 2) {
        $t = $t.Replace($pairs[$i], $pairs[$i + 1])
    }
    if ($t -ne $o) {
        [System.IO.File]::WriteAllText($f, $t, (New-Object System.Text.UTF8Encoding($false)))
        "patched $rel"
    } else {
        "NO CHANGE: $rel"
    }
}

# Holder<MobEffect> lists -> MobEffect in 1.20.1 (getEffect returns MobEffect)
foreach ($rel in @('effects\Reversal\ReversalEffect.java','entity\spells\AcidRainAoe.java','spells\holy\BanishSpell.java')) {
    $f = Join-Path $root $rel
    $t = [System.IO.File]::ReadAllText($f)
    $o = $t
    $t = $t.Replace('.<Holder<MobEffect>>map(', '.map(').Replace('.<Holder>map(', '.map(')
    $t = $t.Replace('List<Holder<MobEffect>>', 'List<MobEffect>')
    if ($t -ne $o) { [System.IO.File]::WriteAllText($f, $t, (New-Object System.Text.UTF8Encoding($false))); "patched holder $rel" }
}
