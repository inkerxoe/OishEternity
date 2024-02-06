package me.inkerxoe.oishplugin.eternity.internal.manager

import me.inkerxoe.oishplugin.eternity.internal.module.ConfigModule
import me.inkerxoe.oishplugin.eternity.internal.module.ScriptModule
import org.bukkit.entity.Player
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.platform.util.killer

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.internal.manager
 *
 * @author InkerXoe
 * @since 2024/2/6 14:15
 */
object ActionManager {
    fun runAction(typ: String, sender: Player?, script: String, event: PlayerDeathEvent?, type: String, args: HashMap<Any, Any>): Boolean {
        when (typ) {
            ConfigModule.options_identifiers_script_KETHER -> {
                // Kether PreAction 动作预设变量
                val defaultArgs = hashMapOf<Any, Any>()
                defaultArgs["player"] = sender!! // 玩家对象
                if (type == "death") {
                    defaultArgs["event"] = event!! // 死亡事件
                    defaultArgs["deathWorld"] = event.entity.world // 死亡世界
                    defaultArgs["level"] = event.entity.level // 死亡等级
                    defaultArgs["location"] = event.entity.location // 死亡点
                    defaultArgs["killer"] = event.killer?:"null" // 击杀者
                }
                val newArgs = (defaultArgs + args) as HashMap<Any, Any>
                return ScriptModule.runActionKe(script, newArgs)
            }
            ConfigModule.options_identifiers_script_JAVASCRIPT -> {
                // JavaScript PreAction 动作预设变量
                val defaultArgs = hashMapOf<Any, Any>()
                defaultArgs["player"] = sender!! // 玩家对象
                if (type == "death") {
                    args["event"] = event!! // 死亡事件
                }
                val newArgs = (defaultArgs + args) as HashMap<Any, Any>
                return ScriptModule.runActionJs(script, newArgs)
            }
        }
        return false
    }
}