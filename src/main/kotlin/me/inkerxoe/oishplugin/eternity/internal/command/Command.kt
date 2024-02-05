package me.inkerxoe.oishplugin.eternity.internal.command

import me.inkerxoe.oishplugin.eternity.common.script.kether.KetherUtil.runActions
import me.inkerxoe.oishplugin.eternity.common.script.kether.KetherUtil.toKetherScript
import me.inkerxoe.oishplugin.eternity.common.script.nashorn.manager.ScriptManager
import me.inkerxoe.oishplugin.eternity.internal.command.subcommand.Reload
import me.inkerxoe.oishplugin.eternity.internal.handle.CentralHandle
import me.inkerxoe.oishplugin.eternity.internal.manager.ConfigManager
import me.inkerxoe.oishplugin.eternity.internal.module.ConfigModule
import me.inkerxoe.oishplugin.eternity.internal.module.ScriptModule
import me.inkerxoe.oishplugin.eternity.utils.ToolsUtil
import me.inkerxoe.oishplugin.eternity.utils.ToolsUtil.toResult
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.adaptCommandSender
import taboolib.common.platform.function.console
import taboolib.common.platform.function.info
import taboolib.expansion.createHelper
import taboolib.module.configuration.util.asMap
import taboolib.module.kether.printKetherErrorMessage
import taboolib.module.lang.sendLang
import taboolib.platform.util.bukkitPlugin
import taboolib.platform.util.sendLang
import kotlin.coroutines.Continuation

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.internal.command
 *
 * @author InkerXoe
 * @since 2024/2/4 09:05
 */

@CommandHeader(
    name = "OishEternity",
    aliases = ["ose", "death"],
    description = "OishEternity Main Command"
)
object Command {
    @CommandBody
    val main = mainCommand { createHelper() }
    @CommandBody
    val help = subCommand { createHelper() }
    @CommandBody
    val runKether = subCommand {
        dynamic {
            execute<CommandSender> { sender, _, content ->
                try {
                    val script = if (content.startsWith("def")) {
                        content
                    } else {
                        "def main = { $content }"
                    }

                    script.toKetherScript().runActions {
                        this.sender = adaptCommandSender(sender)
                        if (sender is Player) {
                            set("player", sender)
                            set("main-hand", sender.equipment?.itemInMainHand)
                            set("off-hand", sender.equipment?.itemInOffHand)
                        }
                    }.thenAccept {
                        sender.sendLang("Plugin-Script-Result-Format", it.toString())
                    }
                } catch (e: Exception) {
                    e.printKetherErrorMessage()
                }
            }
        }
    }
    @CommandBody
    val runJavaScript = subCommand {
        dynamic {
            execute<CommandSender> { sender, _, content ->
                try {
                    ScriptManager.scriptEngine.put("player", sender)
                    val result = ScriptManager.scriptEngine.eval(content)?:""
                    sender.sendLang("Plugin-Script-Result-Format", result)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }
    @CommandBody
    val runPlayDead = subCommand {
        dynamic {
            execute<CommandSender> { _, _, content ->
                val p = bukkitPlugin.server.getPlayer(content)
                if (p == null) {
                    console().sendLang("Plugin-NullPlayer")
                } else {
                    CentralHandle.transmit(null, p, "kill")
                }
            }
        }
    }
    @CommandBody
    val reload = Reload.reload
}