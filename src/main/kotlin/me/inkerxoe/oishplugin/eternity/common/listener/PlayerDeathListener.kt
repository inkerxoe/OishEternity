package me.inkerxoe.oishplugin.eternity.common.listener

import me.inkerxoe.oishplugin.eternity.internal.handle.ActionHandle
import me.inkerxoe.oishplugin.eternity.internal.handle.CheckHandle
import me.inkerxoe.oishplugin.eternity.internal.module.ConfigModule
import me.inkerxoe.oishplugin.eternity.utils.ToolsUtil
import org.bukkit.GameRule
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang
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
        if (ConfigModule.options_death_info) {
            console().sendLang("Plugin-Death-Info-Run", event.entity.player!!.name)
        } else {
            ToolsUtil.debug("监听到玩家${event.entity.player!!.name}死亡 开始处理插件逻辑...")
        }
        val startTime = ToolsUtil.timing()
        // 设置世界死亡掉落
        ToolsUtil.setKeep(event, false)
        // 获取对应配置
        val checkedConfig = CheckHandle.check(event)
        if (checkedConfig.isEmpty()) {
            ToolsUtil.debug("很可惜，玩家${event.entity.player}不存在可行配置.")
        } else {
            // 设置世界死亡不掉落
            ToolsUtil.setKeep(event, true)
            ActionHandle.action(event, checkedConfig)
        }
        val time = ToolsUtil.timing(startTime)
        if (ConfigModule.options_death_info) {
            console().sendLang("Plugin-Death-Info-End", time)
        } else {
            ToolsUtil.debug("插件逻辑执行完毕! 耗时 &6${time} &fms")
        }
    }
}

