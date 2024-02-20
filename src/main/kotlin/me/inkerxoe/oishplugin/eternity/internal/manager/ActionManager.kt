package me.inkerxoe.oishplugin.eternity.internal.manager

import me.inkerxoe.oishplugin.eternity.internal.module.ConfigModule
import me.inkerxoe.oishplugin.eternity.internal.module.ScriptModule
import me.inkerxoe.oishplugin.eternity.utils.ToolsUtil
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.function.console

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.internal.manager
 *
 * @author InkerXoe
 * @since 2024/2/19 上午11:23
 */
object ActionManager {
    private fun runAction(type: String, script: String, args: HashMap<Any, Any>): Boolean {
        ToolsUtil.debug("runAction")
        ToolsUtil.debug("type -> $type")
        when (type) {
            ConfigModule.options_identifiers_script_KETHER -> {
                return ScriptModule.runActionKe(script, args)
            }
            ConfigModule.options_identifiers_script_JAVASCRIPT -> {
                return ScriptModule.runActionJs(script, args)
            }
        }
        return false
    }
    fun runActionHandle(actionConfig: Map<String, Any?>, args: HashMap<Any, Any>): Boolean {
        val type = actionConfig["type"].toString()
        ToolsUtil.debug("set action.type to $type")
        val script = actionConfig["script"].toString()
        ToolsUtil.debug("set action.script to $script")
        ToolsUtil.debug("args -> $args")
        return ActionManager.runAction(type, script, args)
    }
}