package me.inkerxoe.oishplugin.eternity.internal.manager

import me.inkerxoe.oishplugin.eternity.OishEternity
import me.inkerxoe.oishplugin.eternity.internal.module.ConfigModule
import me.inkerxoe.oishplugin.eternity.utils.createIfNotExists
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.internal.manager
 *
 * @author InkerXoe
 * @since 2024/2/4 14:47
 */
object ConfigManager {
    val options_debug
        get() = OishEternity.setting.getBoolean("options.debug")

    @Awake(LifeCycle.LOAD)
    private fun load(){
        createIfNotExists("workspace", "example","script/example.js")
        ConfigModule.loadConfigs()
    }
    @Awake(LifeCycle.DISABLE)
    private fun disable(){
        createIfNotExists("workspace", "example","script/example.js")
        ConfigModule.loadConfigs()
    }
}