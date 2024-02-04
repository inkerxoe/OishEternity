package me.inkerxoe.oishplugin.eternity.common.listener

import me.inkerxoe.oishplugin.eternity.internal.handle.CentralHandle
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.common.listener
 *
 * @author InkerXoe
 * @since 2024/2/3 21:51
 */
object PlayerDeathListener {
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun listener(event: PlayerDeathEvent) {
        CentralHandle.transmit(event, null,"death")
    }
}

