package me.inkerxoe.oishplugin.eternity.internal.module

import me.inkerxoe.oishplugin.eternity.utils.ToolsUtil
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common5.cbool

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.internal.module
 *
 * @author InkerXoe
 * @since 2024/2/19 下午11:47
 */
object SpawnModule {
    fun checkLocation(event: PlayerDeathEvent, config:  Map<String, Any?>): Location? {
        if (config["enable"].cbool) return null
        val type = config["type"].toString()
        val info = config["info"].toString()
        when (type) {
            "death" -> {
                return event.entity.location
            }
            "coo" -> {
                val world = info.split(" ").last().toString()
                val xyz = info.removeSuffix(" $world").split(" ")
                val x = xyz[0].toDouble()
                val y = xyz[1].toDouble()
                val z = xyz[2].toDouble()
                return Location(Bukkit.getWorld(world), x, y, z)
            }
            "loc" -> {
                val bedSpawnLocation = event.entity.player?.world?.spawnLocation
                if (bedSpawnLocation is Location) {
                    return bedSpawnLocation
                }
                ToolsUtil.debug("当前世界不存在重生点 -> ${event.entity.world.name}")
                return event.entity.location
            }
            else -> throw IllegalArgumentException("Invalid spawn type")
        }
    }
}