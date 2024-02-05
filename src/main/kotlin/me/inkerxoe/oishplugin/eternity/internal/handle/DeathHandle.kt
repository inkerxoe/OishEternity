package me.inkerxoe.oishplugin.eternity.internal.handle

import me.inkerxoe.oishplugin.eternity.internal.manager.ConfigManager
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.function.console
import taboolib.common.platform.function.info
import taboolib.module.configuration.util.asMap

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.internal.handle
 *
 * @author InkerXoe
 * @since 2024/2/5 11:52
 */
object DeathHandle {
    fun preHandle(event: PlayerDeathEvent) {
        // check handle
        for ((key, value) in ConfigManager.map) {
            info("---")
            info("key -> $key")
            info("value -> $value")
        }
    }
}