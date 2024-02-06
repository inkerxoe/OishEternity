package me.inkerxoe.oishplugin.eternity.internal.module

import me.inkerxoe.oishplugin.eternity.OishEternity
import org.bukkit.entity.Player
import taboolib.common.util.asList
import taboolib.common5.cbool
import taboolib.module.configuration.util.asMap
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
        if (!regionTerritory(region, sender)) return false

        // region custom
        val custom = config["custom"].asMap()
        return regionCustom(region, sender)
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
                val typ = custom["type"].toString()
                val scr = custom["script"].toString()

                when (typ) {
                    ConfigModule.options_identifiers_script_KETHER -> {
                        val args = hashMapOf<Any, Any>()
                        args["player"] = player
                        return ScriptModule.runActionKe(scr, args)
                    }
                    ConfigModule.options_identifiers_script_JAVASCRIPT -> {
                        val args = hashMapOf<Any, Any>()
                        args["player"] = player
                        return ScriptModule.runActionJs(scr, args)
                    }
                }
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
                // TODO
            }
            ConfigModule.options_identifiers_region_territory_type_custom -> {
                // TODO
            }
        }


        return false
    }

    private fun regionCustom(map:  Map<String, Any?>, player: Player): Boolean {
        val enable = map["enable"].cbool
        if (!enable) return false

        return false
    }
}