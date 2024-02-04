package me.inkerxoe.oishplugin.eternity.common.script.nashorn.manager

import me.inkerxoe.oishplugin.eternity.common.script.nashorn.script.CompiledScript
import me.inkerxoe.oishplugin.eternity.internal.manager.HookerManager.nashornHooker
import me.inkerxoe.oishplugin.eternity.utils.ConfigUtils.getAllFiles
import org.bukkit.Bukkit
import java.io.File

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.common.script.nashorn.manager
 *
 * @author InkerXoe
 * @since 2024/2/4 09:56
 */
/**
 * 脚本文件管理器, 用于管理所有js节点的脚本文件, 同时提供公用ScriptEngine用于解析公式节点内容
 *
 * @constructor 构建脚本文件管理器
 */
object ScriptManager {
    /**
     * 获取公用ScriptEngine
     */
    val scriptEngine = nashornHooker.getNashornEngine()

    /**
     * 获取所有已编译的js脚本文件及路径
     */
    val compiledScripts = HashMap<String, CompiledScript>()

    init {
        // 加载全部脚本
        loadScripts()
    }

    /**
     * 加载全部脚本
     */
    private fun loadScripts() {
        for (file in getAllFiles("workspace${File.separator}scripts")) {
            val fileName =
                file.path.replace("plugins${File.separator}OishEternity${File.separator}workspace${File.separator}scripts${File.separator}", "")
            try {
                compiledScripts[fileName] = CompiledScript(file)
            } catch (error: Throwable) {
                error.printStackTrace()
            }
        }
    }

    /**
     * 重载脚本管理器
     */
    fun reload() {
        compiledScripts.clear()
        loadScripts()
    }
}