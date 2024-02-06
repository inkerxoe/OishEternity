package me.inkerxoe.oishplugin.eternity.internal.module

import me.inkerxoe.oishplugin.eternity.OishEternity
import me.inkerxoe.oishplugin.eternity.common.hook.griefdefender.GriefDefenderHooker
import me.inkerxoe.oishplugin.eternity.common.hook.griefdefender.impl.GriefDefenderHookerImpl
import me.inkerxoe.oishplugin.eternity.common.hook.residence.impl.ResidenceHookerImpl
import me.inkerxoe.oishplugin.eternity.common.hook.worldguard.impl.WorldGuardHookerImpl
import me.inkerxoe.oishplugin.eternity.internal.manager.CustomManager
import me.inkerxoe.oishplugin.eternity.internal.manager.CustomManager.runCustom
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.common.platform.function.console
import taboolib.common.util.asList
import taboolib.common5.cbool
import taboolib.module.configuration.util.asMap
import taboolib.module.lang.sendLang
import taboolib.platform.util.bukkitPlugin

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.internal.module
 *
 * @author InkerXoe
 * @since 2024/2/5 15:39
 */
object RegionModule {
    fun handle(config: Map<String, Any?>, sender: Player): Boolean {
        // region enable
        val enable = config["enable"].cbool
        if (!enable) return false

        // region world
        val region = config["world"].asMap()
        if (!regionWorld(region, sender)) return false

        // region territory
        val territory = config["territory"].asMap()
        if (!regionTerritory(territory, sender)) return false

        // region custom
        val custom = config["custom"].asMap()
        return runCustom(custom, sender)
    }

    private fun regionWorld(map:  Map<String, Any?>, player: Player): Boolean {
        val enable = map["enable"].cbool
        if (!enable) return false
        val type = map["type"].toString()
        val info = map["info"]!!.asList()

        when (type) {
            ConfigModule.options_identifiers_region_world_type_portion -> {
                val deathWorld = player.lastDeathLocation!!.world!!.name
                if (deathWorld in info) return true
            }
            ConfigModule.options_identifiers_region_world_type_custom -> {
                // 自定义逻辑
                val custom = map["custom"].asMap()

                return CustomManager.runAction(custom, player)
            }
        }
        return false
    }

    private fun regionTerritory(map:  Map<String, Any?>, player: Player): Boolean {
        val enable = map["enable"].cbool
        if (!enable) return false
        val type = map["type"].toString()
        val adapter = map["adapter"].toString()
        val info = map["info"]!!.asList()

        when (type) {
            ConfigModule.options_identifiers_region_territory_type_portion -> {
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
            }
            ConfigModule.options_identifiers_region_territory_type_custom -> {
                // 自定义逻辑
                val custom = map["custom"].asMap()
                return CustomManager.runAction(custom, player)
            }
        }
        return false
    }
}