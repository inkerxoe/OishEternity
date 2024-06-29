package me.inkerxoe.oishplugin.eternity.internal.module

import me.inkerxoe.oishplugin.eternity.OishEternity
import me.inkerxoe.oishplugin.eternity.OishEternity.plugin
import me.inkerxoe.oishplugin.eternity.utils.ToolsUtil
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common5.cbool
import taboolib.common5.clong
import taboolib.module.configuration.util.asMap
import taboolib.platform.util.isNotAir

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.internal.module
 *
 * @author InkerXoe
 * @since 2024/2/19 下午10:52
 */
object RelicModule {
    fun runRelic(config: Map<String, Any?>, player: Player, dropItem: List<ItemStack?>) {
        if (!config["enable"].cbool) return
        val type = config["type"].toString()
        ToolsUtil.debug("type -> $type")
        when (type) {
            "normal" -> {
                ToolsUtil.debug("Relic Type -> Normal")
                val world = player.world
                val loc = player.location
                for (item in dropItem) {
                    if (item.isNotAir())
                        world.dropItem(loc, item)
                }
            }
            "chest" -> {
                //TODO
            }
            "fancy" -> {
                ToolsUtil.debug("Relic Type -> Fancy")
                val loc = player.location
                val fancy = config["fancy_drop"].asMap()
                val offset = fancy["offset"].asMap()
                val offsetX = offset["x"].toString()
                ToolsUtil.debug("offsetX -> $offsetX")
                val offsetY = offset["y"].toString()
                ToolsUtil.debug("offsetY -> $offsetY")
                val angle = fancy["angle"].asMap()
                val angleType = angle["type"].toString()
                ToolsUtil.debug("angleType -> $angleType")
                val delayTicks = fancy["delay"].clong
                ToolsUtil.debug("delayTicks -> $delayTicks")
                ItemUtil.dropItems(plugin, dropItem.filterNotNull(), loc, offsetX, offsetY, angleType, delayTicks)
            }
            "redeem" -> {
                //TODO
            }
        }
    }
}