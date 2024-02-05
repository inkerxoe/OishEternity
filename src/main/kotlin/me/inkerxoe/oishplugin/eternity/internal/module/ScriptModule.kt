package me.inkerxoe.oishplugin.eternity.internal.module

import me.inkerxoe.oishplugin.eternity.common.script.kether.KetherUtil.runActions
import me.inkerxoe.oishplugin.eternity.common.script.kether.KetherUtil.toKetherScript
import me.inkerxoe.oishplugin.eternity.common.script.nashorn.manager.ScriptManager
import me.inkerxoe.oishplugin.eternity.common.script.nashorn.script.CompiledScript
import me.inkerxoe.oishplugin.eternity.internal.manager.HookerManager.nashornHooker
import me.inkerxoe.oishplugin.eternity.utils.ToolsUtil.toResult
import taboolib.module.kether.printKetherErrorMessage
import taboolib.platform.util.sendLang

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.internal.module
 *
 * @author InkerXoe
 * @since 2024/2/5 12:13
 */
object ScriptModule {
    private val engine by lazy { nashornHooker.getNashornEngine() }

    fun runActionKe(script: String, args: HashMap<Any, Any>): Boolean {
        var result = false
        try {
            val scr = if (script.startsWith("def")) {
                script
            } else {
                "def main = { $script }"
            }

            scr.toKetherScript().runActions {
                set("args", args)
            }.thenAccept {
                result = this.toResult()
            }
        } catch (e: Exception) {
            e.printKetherErrorMessage()
        }
        return result
    }

    fun runActionJs(script: String, args: HashMap<Any, Any>): Boolean {
        var result = false

        try {
            engine.put("args", args)
            result = engine.eval(script).toResult()
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return result
    }
}