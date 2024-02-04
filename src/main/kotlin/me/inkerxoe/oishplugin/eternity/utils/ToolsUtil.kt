package me.inkerxoe.oishplugin.eternity.utils

import me.inkerxoe.oishplugin.eternity.internal.manager.ConfigManager
import taboolib.common.platform.function.console
import taboolib.common5.Coerce
import taboolib.module.lang.sendLang

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.utils
 *
 * @author InkerXoe
 * @since 2024/2/3 15:38
 */
object ToolsUtil {
    fun printLogo() {
        console().sendMessage("§b   ____  _     __   ______               _ __      ")
        console().sendMessage("§b  / __ \\(_)__ / /  / __/ /____ _______  (_) /___ __")
        console().sendMessage("§b / /_/ / (_-</ _ \\/ _// __/ -_) __/ _ \\/ / __/ // /")
        console().sendMessage("§b \\____/_/___/_//_/___/\\__/\\__/_/ /_//_/_/\\__/\\_, / ")
        console().sendMessage("§b                                            /___/  ")
    }

    fun debug(text: String) {
        if (ConfigManager.options_debug) {
            console().sendLang("Plugin-Debug", text)
        }
    }

    fun timing(): Long {
        return System.nanoTime()
    }

    fun timing(start: Long): Double {
        return Coerce.format((System.nanoTime() - start).div(1000000.0))
    }
}