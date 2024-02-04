package me.inkerxoe.oishplugin.eternity.internal.manager

import me.inkerxoe.oishplugin.eternity.common.script.nashorn.hook.NashornHooker
import me.inkerxoe.oishplugin.eternity.common.script.nashorn.hook.impl.LegacyNashornHookerImpl
import me.inkerxoe.oishplugin.eternity.common.script.nashorn.hook.impl.NashornHookerImpl

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.internal.manager
 *
 * @author InkerXoe
 * @since 2024/2/4 09:28
 */
/**
 * 插件兼容管理器, 用于尝试与各个软依赖插件取得联系
 */
object HookerManager {
    private fun check(clazz: String): Boolean {
        return try {
            Class.forName(clazz)
            true
        } catch (error: Throwable) {
            false
        }
    }

    val nashornHooker: NashornHooker =
        when {
            // jdk自带nashorn
            check("jdk.nashorn.api.scripting.NashornScriptEngineFactory") -> LegacyNashornHookerImpl()
            // 主动下载nashorn
            else -> NashornHookerImpl()
        }
}