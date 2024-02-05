package me.inkerxoe.oishplugin.eternity.internal.module

import org.bukkit.entity.Player
import taboolib.common.util.asList
import taboolib.common5.cbool
import taboolib.module.configuration.util.asMap

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

        }
        return true
    }

    private fun regionTerritory(map:  Map<String, Any?>, player: Player): Boolean {
        val enable = map["enable"].cbool
        if (!enable) return false

        return true
    }

    private fun regionCustom(map:  Map<String, Any?>, player: Player): Boolean {
        val enable = map["enable"].cbool
        if (!enable) return false

        return true
    }
}