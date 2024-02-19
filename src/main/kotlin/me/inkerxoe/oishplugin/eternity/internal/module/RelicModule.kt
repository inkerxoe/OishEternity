package me.inkerxoe.oishplugin.eternity.internal.module

import me.inkerxoe.oishplugin.eternity.OishEternity.plugin
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common5.cbool
import taboolib.common5.clong
import taboolib.module.configuration.util.asMap

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.internal.module
 *
 * @author InkerXoe
 * @since 2024/2/19 下午10:52
 */
object RelicModule {
    fun runRelic(config: Map<String, Any?>, player: Player, dropItem: List<ItemStack>) {
        if (!config["enable"].cbool) return
        val type = config["type"].toString()
        when (type) {
            "drop" -> {
                for (item in dropItem) {
                    val world = player.world
                    val loc = player.location
                    world.dropItem(loc, item)
                }
            }
            "chest" -> {
                //TODO
            }
            "fancy" -> {
                val loc = player.location
                val fancy = config["fancy_drop"].asMap()
                val offsetX = fancy["offsetX"].toString()
                val offsetY = fancy["offsetY"].toString()
                val angle = fancy["angle"].asMap()
                val angleType = angle["type"].toString()
                val delayTicks = fancy["delay"].clong
                ItemUtil.dropItems(plugin, dropItem, loc, offsetX, offsetY, angleType, delayTicks)
            }
            "redeem" -> {
                //TODO
            }
        }
    }
}