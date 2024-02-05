package me.inkerxoe.oishplugin.eternity.internal.handle

import me.inkerxoe.oishplugin.eternity.utils.ToolsUtil
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common5.clong
import taboolib.platform.util.killer

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.internal.handle
 *
 * @author InkerXoe
 * @since 2024/2/4 14:38
 */
object CentralHandle {
    private var startTime: Long = 0.0.clong
    fun transmit(e: PlayerDeathEvent ?= null, p: Player ?= null, type: String) {

        startTime = ToolsUtil.timing()

        var player: Player? = null
        var deathWorld: World? = null
        var location: Location? = null
        var killer: LivingEntity? = null

        when (type) {
            "death" -> {
                player = e!!.entity.player!!
                deathWorld = e.entity.world
                location = e.entity.location
                killer = e.killer
            }
            "kill" -> {
                player = p!!
                deathWorld = p.world
                location = p.location
                killer = null
            }
        }

        val level = player!!.level

        preHandle(type, player, level, deathWorld, location, killer, e)
    }

    private fun preHandle(type: String, player: Player?, level: Int, deathWorld: World?, location: Location?, killer: LivingEntity?, event: PlayerDeathEvent?) {
        ToolsUtil.debug("监听到玩家${player!!.name}死亡 开始处理插件逻辑...")
        ToolsUtil.debug("Handle Type -> $type")
        ToolsUtil.debug("player -> $player")
        ToolsUtil.debug("level -> $level")
        ToolsUtil.debug("deathWorld -> $deathWorld")
        ToolsUtil.debug("location -> $location")
        ToolsUtil.debug("killer -> ${killer?:"null"}")
        ToolsUtil.debug("event -> $event")
        postHandle(event, player, type)
    }

    private fun postHandle(event: PlayerDeathEvent? = null, player: Player? = null, type: String) {
        when(type) {
            "death" -> {
                deathHandle(event!!)
            }
            "kill" -> {
                killHandle(player!!)
            }
        }
        ToolsUtil.debug("插件逻辑执行完毕! 耗时 &6${ToolsUtil.timing(startTime)} &fms")
    }

    private fun deathHandle(event: PlayerDeathEvent) {
        DeathHandle.preHandle(event)
    }

    private fun killHandle(player: Player) {

    }
}