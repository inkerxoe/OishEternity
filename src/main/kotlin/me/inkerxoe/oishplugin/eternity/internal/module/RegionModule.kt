package me.inkerxoe.oishplugin.eternity.internal.module

import me.inkerxoe.oishplugin.eternity.OishEternity
import me.inkerxoe.oishplugin.eternity.common.hook.griefdefender.impl.GriefDefenderHookerImpl
import me.inkerxoe.oishplugin.eternity.common.hook.residence.impl.ResidenceHookerImpl
import me.inkerxoe.oishplugin.eternity.common.hook.worldguard.impl.WorldGuardHookerImpl
import org.bukkit.entity.Player
import taboolib.common.platform.function.console
import taboolib.common.util.asList
import taboolib.common5.cbool
import taboolib.module.configuration.util.asMap
import taboolib.module.lang.sendLang

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.internal.module
 *
 * @author InkerXoe
 * @since 2024/2/19 下午12:03
 */
object RegionModule {
    fun handle(config: Map<String, Any?>, player: Player): Boolean {
        // region enable
        val enable = config["enable"].cbool
        if (!enable) return false

        // region world
        val region = config["world"].asMap()
        if (!regionWorld(region, player)) return false

        // region territory
        val territory = config["territory"].asMap()
        if (!regionTerritory(territory, player)) return false

        return true
    }

    private fun regionWorld(map:  Map<String, Any?>, player: Player): Boolean {
        val enable = map["enable"].cbool
        if (!enable) return false
        val info = map["info"]!!.asList()
        val deathWorld = player.location.world!!.name
        if (deathWorld in info) return true
        return false
    }

    private fun regionTerritory(map:  Map<String, Any?>, player: Player): Boolean {
        val enable = map["enable"].cbool
        if (!enable) return false
        val adapter = map["adapter"].toString()
        val info = map["info"]!!.asList()
        when (adapter) {
            ConfigModule.options_identifiers_region_territory_type_adapter_grief_defender -> {
                if (OishEternity.hookerGriefDefender) {
                    if (GriefDefenderHookerImpl().getLocation(player) in info) return true
                } else {
                    console().sendLang("Plugin-Hooker-Not", "GriefDefender")
                    return true
                }
            }
            ConfigModule.options_identifiers_region_territory_type_adapter_world_guard -> {
                if (OishEternity.hookerWorldGuard) {
                    if (WorldGuardHookerImpl().getLocation(player) in info) return true
                } else {
                    console().sendLang("Plugin-Hooker-Not", "WorldGuard")
                    return true
                }
            }
            ConfigModule.options_identifiers_region_territory_type_adapter_residence -> {
                if (OishEternity.hookerResidence) {
                    if (ResidenceHookerImpl().getLocation(player) in info) return true
                } else {
                    console().sendLang("Plugin-Hooker-Not", "Residence")
                    return true
                }
            }
        }
        return false
    }
}