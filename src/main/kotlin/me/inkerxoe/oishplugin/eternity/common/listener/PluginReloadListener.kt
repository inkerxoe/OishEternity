package me.inkerxoe.oishplugin.eternity.common.listener

import me.inkerxoe.oishplugin.eternity.OishEternity
import me.inkerxoe.oishplugin.eternity.common.event.PluginReloadEvent
import me.inkerxoe.oishplugin.eternity.common.script.nashorn.manager.ScriptManager
import me.inkerxoe.oishplugin.eternity.internal.manager.ConfigManager
import me.inkerxoe.oishplugin.eternity.internal.module.ConfigModule
import taboolib.common.platform.event.SubscribeEvent

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.common.listener
 *
 * @author InkerXoe
 * @since 2024/2/5 09:33
 */
object PluginReloadListener {
    @SubscribeEvent
    fun listener(event: PluginReloadEvent) {
        // 重载默认配置
        OishEternity.setting.reload()
        // 重载脚本管理器
        ScriptManager.reload()
        // 重载配置管理器
        ConfigManager.reload()
        // 重载配置加载模块
        ConfigModule.reload()
    }
}