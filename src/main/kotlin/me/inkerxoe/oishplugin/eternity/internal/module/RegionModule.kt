package me.inkerxoe.oishplugin.eternity.internal.module

import org.bukkit.entity.Player
import taboolib.common5.cbool

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.internal.module
 *
 * @author InkerXoe
 * @since 2024/2/5 15:39
 */
object RegionModule {
    fun handle(config: Map<String, Any?>, sender: Player): Boolean {
        // region enable
        val enable = config["enable"].cbool
        if (!enable) return false








        return true
    }
}