package me.inkerxoe.oishplugin.eternity.internal

import me.inkerxoe.oishplugin.eternity.OishEternity.plugin
import me.inkerxoe.oishplugin.eternity.internal.manager.HookerManager
import me.inkerxoe.oishplugin.eternity.utils.ToolsUtil
import org.bukkit.Bukkit
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Platform
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang
import taboolib.module.metrics.Metrics

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.internal
 *
 * @author InkerXoe
 * @since 2024/2/3 15:31
 */
object OishEternityLoader {
    @Awake(LifeCycle.LOAD)
    fun load() {
        console().sendMessage("")
        console().sendLang("Plugin-Loading", Bukkit.getServer().version)
        console().sendMessage("")
        Metrics(20902, plugin.description.version, Platform.BUKKIT)
    }

    @Awake(LifeCycle.ENABLE)
    fun enable() {
        ToolsUtil.printLogo()
        HookerManager.ResidenceHooker
        HookerManager.GriefDefenderHooker
        HookerManager.WorldGuardHooker
        console().sendLang("Plugin-Enabled")
        ToolsUtil.debug("Debug模式已开启.")
    }

    @Awake(LifeCycle.DISABLE)
    fun disable() {
        console().sendLang("Plugin-Disable")
    }
}