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
    fun runActionHandle(actionConfig: Map<String, Any?>, args: HashMap<Any, Any>): Boolean {
        val type = actionConfig["type"]?.toString() ?: return false.also {
            ToolsUtil.debug("ActionManager - Missing 'type' in action configuration.")
        }
        val script = actionConfig["script"]?.toString() ?: return false.also {
            ToolsUtil.debug("ActionManager - Missing 'script' in action configuration.")
        }
        return runAction(type, script, args)
    }

    private fun runAction(type: String, script: String, args: HashMap<Any, Any>): Boolean {
        return when (type) {
            ConfigModule.options_identifiers_script_KETHER -> {
                ToolsUtil.debug("Running Kether script action.")
                ScriptModule.runActionKe(script, args)
            }
            ConfigModule.options_identifiers_script_JAVASCRIPT -> {
                ToolsUtil.debug("Running JavaScript script action.")
                ScriptModule.runActionJs(script, args)
            }
            else -> false.also {
                ToolsUtil.debug("Unsupported action type: $type")
            }
        }
    }
}