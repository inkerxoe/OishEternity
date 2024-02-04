package me.inkerxoe.oishplugin.eternity.internal.manager

import me.inkerxoe.oishplugin.eternity.OishEternity

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
}