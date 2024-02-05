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
object SelectModule {
    fun handle(config: Map<String, Any?>, sender: Player): Boolean {
        // select enable
        val enable = config["enable"].cbool
        if (!enable) return false

        // select player
        val player = config["player"].asMap()
        if (!selectPlayer(player, sender)) return false

        // select perm
        val perm = config["perm"].asMap()
        if(!selectPerm(perm, sender)) return false

        // select custom
        val custom = config["custom"].asMap()
        return selectCustom(custom, sender)
    }

    private fun selectPlayer(map:  Map<String, Any?>, player: Player): Boolean {
        val enable = map["enable"].cbool
        if (!enable) return false
        val conf = map["config"].asMap()
        val type = conf["type"].toString()
        val info = conf["info"]!!.asList()
        val display = player.name

        when (type) {
            ConfigModule.options_identifiers_select_player_type_appoint -> {
                if (display in info) {
                    return true
                }
            }
            ConfigModule.options_identifiers_select_player_type_all -> {
                return true
            }
            ConfigModule.options_identifiers_select_player_type_custom -> {
                // 自定义逻辑
                val custom = conf["custom"].asMap()
                val typ = custom["type"].toString()
                val scr = custom["script"].toString()
                when (typ) {
                    ConfigModule.options_identifiers_script_JAVASCRIPT -> {
                        val args = hashMapOf<Any, Any>()
                        args["player"] = player
                        return ScriptModule.runActionJs(scr, args)
                    }
                    ConfigModule.options_identifiers_script_KETHER -> {
                        val args = hashMapOf<Any, Any>()
                        args["player"] = player
                        return ScriptModule.runActionKe(scr, args)
                    }
                }
            }
        }
        return true
    }

    private fun selectPerm(map:  Map<String, Any?>, player: Player): Boolean {
        val enable = map["enable"].cbool
        if (!enable) return false
        val conf = map["config"].asMap()
        val type = conf["type"].toString()
        val info = conf["info"]!!.asList()
        when (type) {
            ConfigModule.options_identifiers_select_perm_type_appoint -> {
                for (perm in info) {
                    if (!player.hasPermission(perm)) return false
                }
            }
            ConfigModule.options_identifiers_select_perm_type_portion -> {
                for (perm in info) {
                    if (player.hasPermission(perm)) return true
                }
            }
            ConfigModule.options_identifiers_select_perm_type_custom -> {
                // 自定义逻辑
                val custom = conf["custom"].asMap()
                val typ = custom["type"].toString()
                val scr = custom["script"].toString()
                when (typ) {
                    ConfigModule.options_identifiers_script_JAVASCRIPT -> {
                        val args = hashMapOf<Any, Any>()
                        args["player"] = player
                        return ScriptModule.runActionJs(scr, args)
                    }
                    ConfigModule.options_identifiers_script_KETHER -> {
                        val args = hashMapOf<Any, Any>()
                        args["player"] = player
                        return ScriptModule.runActionKe(scr, args)
                    }
                }
            }
        }
        return true
    }

    private fun selectCustom(map:  Map<String, Any?>, player: Player): Boolean {
        val enable = map["enable"].cbool
        if (!enable) return false
        val type = map["type"].toString()
        val script = map["script"].toString()
        when (type) {
            ConfigModule.options_identifiers_script_JAVASCRIPT -> {
                val args = hashMapOf<Any, Any>()
                args["player"] = player
                return ScriptModule.runActionJs(script, args)
            }
            ConfigModule.options_identifiers_script_KETHER -> {
                val args = hashMapOf<Any, Any>()
                args["player"] = player
                return ScriptModule.runActionKe(script, args)
            }
        }
        return true
    }
}