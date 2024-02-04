package me.inkerxoe.oishplugin.eternity.common.listener

import me.inkerxoe.oishplugin.eternity.internal.handle.CentralHandle
import me.inkerxoe.oishplugin.eternity.utils.ToolsUtil
import org.bukkit.GameRule
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import javax.tools.Tool

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
        // 设置世界死亡不掉落
        try {
            event.entity.world.setGameRule(GameRule.KEEP_INVENTORY, true)
        } catch (error: NoClassDefFoundError) {
            ToolsUtil.debug("GameRule -> ${error.cause}")
            ToolsUtil.debug("手动设置中...")
            event.keepInventory = true
            ToolsUtil.debug("keepInventory -> ${event.keepInventory}")
            event.keepLevel = true
            ToolsUtil.debug("keepLevel -> ${event.keepLevel}")
        }

        CentralHandle.transmit(event, null,"death")
    }
}

