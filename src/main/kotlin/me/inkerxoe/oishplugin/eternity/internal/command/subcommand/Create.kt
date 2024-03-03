package me.inkerxoe.oishplugin.eternity.internal.command.subcommand

import me.inkerxoe.oishplugin.eternity.OishEternity
import me.inkerxoe.oishplugin.eternity.utils.ConfigUtil.getFileOrCreate
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.subCommand
import taboolib.platform.util.bukkitPlugin
import taboolib.platform.util.sendLang
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.internal.command.subcommand
 *
 * @author InkerXoe
 * @since 2024/3/3 10:58
 */
object Create {
    val create = subCommand {
        dynamic {
            execute<CommandSender> { sender, _, context ->
                val result = createConfig(context)
                if (result == null) {
                    sender.sendLang("Command-Create-True")
                }else {
                    sender.sendLang("Command-Create-False", result)
                }
            }
        }
    }

    private fun createConfig(name: String): String? {
        var len: Int
        val buf = ByteArray(1024)
        val outFile = File(bukkitPlugin.dataFolder, "workspace/${name}.yml")
        val inputStream = OishEternity.plugin.getResource("workspace/example.yml")
        if (outFile.exists()) { return "File already exists" }
        else {
            getFileOrCreate("workspace/${name}.yml")
        }
        try {
            val fileOutputStream = FileOutputStream(outFile)
            while (inputStream!!.read(buf).also { len = it } > 0) {
                fileOutputStream.write(buf, 0, len)
            }
            fileOutputStream.close()
            inputStream.close()
        } catch (error: IOException) {
            return error.cause.toString()
        }
        return null
    }
}