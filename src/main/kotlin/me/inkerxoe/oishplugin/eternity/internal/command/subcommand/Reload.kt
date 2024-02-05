package me.inkerxoe.oishplugin.eternity.internal.command.subcommand

import me.inkerxoe.oishplugin.eternity.common.event.PluginReloadEvent
import me.inkerxoe.oishplugin.eternity.utils.ToolsUtil
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit
import taboolib.platform.util.sendLang

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.internal.command.subcommand
 *
 * @author InkerXoe
 * @since 2024/2/5 09:31
 */
object Reload {
    val reload = subCommand {
        execute<CommandSender> { sender, _, _ ->
            reloadCommand(sender)
        }
    }

    private fun reloadCommand(sender: CommandSender) {
        submit(async = true) {
            PluginReloadEvent.call()
            sender.sendLang("Plugin-Reloaded")
            ToolsUtil.debug("Debug模式已开启.")
        }
    }
}