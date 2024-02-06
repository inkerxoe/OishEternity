package me.inkerxoe.oishplugin.eternity.internal.manager

import me.inkerxoe.oishplugin.eternity.common.hook.griefdefender.GriefDefenderHooker
import me.inkerxoe.oishplugin.eternity.common.hook.griefdefender.impl.GriefDefenderHookerImpl
import me.inkerxoe.oishplugin.eternity.common.hook.residence.ResidenceHooker
import me.inkerxoe.oishplugin.eternity.common.hook.residence.impl.ResidenceHookerImpl
import me.inkerxoe.oishplugin.eternity.common.hook.worldguard.impl.WorldGuardHookerImpl
import me.inkerxoe.oishplugin.eternity.common.script.nashorn.hook.NashornHooker
import me.inkerxoe.oishplugin.eternity.common.script.nashorn.hook.impl.LegacyNashornHookerImpl
import me.inkerxoe.oishplugin.eternity.common.script.nashorn.hook.impl.NashornHookerImpl
import org.bukkit.Bukkit
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

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


    val ResidenceHooker: ResidenceHooker? =
        if (Bukkit.getPluginManager().isPluginEnabled("Residence")) {
            console().sendLang("Plugin-Hooker-True", "Residence")
            try {
                ResidenceHookerImpl()
            } catch (error: Throwable) {
                null
            }
        } else {
            console().sendLang("Plugin-Hooker-False", "Residence")
            null
        }
    val GriefDefenderHooker: GriefDefenderHooker? =
        if (Bukkit.getPluginManager().isPluginEnabled("GriefDefender")) {
            console().sendLang("Plugin-Hooker-True", "GriefDefender")
            try {
                GriefDefenderHookerImpl()
            } catch (error: Throwable) {
                null
            }
        } else {
            console().sendLang("Plugin-Hooker-False", "GriefDefender")
            null
        }
    val WorldGuardHooker: WorldGuardHookerImpl? =
        if (Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
            console().sendLang("Plugin-Hooker-True", "WorldGuard")
            try {
                WorldGuardHookerImpl()
            } catch (error: Throwable) {
                null
            }
        } else {
            console().sendLang("Plugin-Hooker-False", "WorldGuard")
            null
        }
}