package me.inkerxoe.oishplugin.eternity.internal.manager

import me.inkerxoe.oishplugin.eternity.internal.module.ConfigModule
import me.inkerxoe.oishplugin.eternity.internal.module.ScriptModule
import org.bukkit.entity.Player
import taboolib.common5.cbool

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.internal.manager
 *
 * @author InkerXoe
 * @since 2024/2/6 11:57
 */
object CustomManager {
    fun runCustom(map:  Map<String, Any?>, player: Player): Boolean {
        val enable = map["enable"].cbool
        if (!enable) return false
        return  runAction(map, player)
    }
    fun runAction(map:  Map<String, Any?>, player: Player): Boolean {
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
        return false
    }
}