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
 * @since 2024/2/19 上午11:33
 */
object SelectModule {
    fun handle(config: Map<String, Any?>, player: Player): Boolean {
        // select enable
        val enable = config["enable"].cbool
        if (!enable) return false

        // select player
        val playerConfig = config["player"].asMap()
        if (!selectPlayer(playerConfig, player)) return false

        // select perm
        val perm = config["perm"].asMap()
        if(!selectPerm(perm, player)) return false

        return true
    }

    private fun selectPlayer(map:  Map<String, Any?>, player: Player): Boolean {
        val enable = map["enable"].cbool
        if (!enable) return false
        val conf = map["config"].asMap()
        val info = conf["info"]!!.asList()
        val display = player.name
        if (display in info) return true
        return false
    }

    private fun selectPerm(map:  Map<String, Any?>, player: Player): Boolean {
        val enable = map["enable"].cbool
        if (!enable) return false
        val conf = map["config"].asMap()
        val type = conf["type"].toString()
        val info = conf["info"]!!.asList()
        when (type) {
            ConfigModule.options_identifiers_select_perm_type_all -> {
                for (perm in info) {
                    if (!player.hasPermission(perm)) return false
                }
            }
            ConfigModule.options_identifiers_select_perm_type_portion -> {
                for (perm in info) {
                    if (player.hasPermission(perm)) return true
                }
            }
        }
        return false
    }
}