package me.inkerxoe.oishplugin.eternity.internal.module

import me.inkerxoe.oishplugin.eternity.utils.ToolsUtil
import me.inkerxoe.oishplugin.eternity.utils.ToolsUtil.debug
import me.inkerxoe.oishplugin.eternity.utils.ToolsUtil.parsePercent
import me.inkerxoe.oishplugin.eternity.utils.ToolsUtil.toMaterial
import org.apache.commons.lang.mutable.MutableInt
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.util.asList
import taboolib.common5.cbool
import taboolib.common5.cfloat
import taboolib.common5.cint
import taboolib.module.configuration.util.asMap
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.getItemTag
import taboolib.platform.util.hasLore
import java.util.concurrent.ThreadLocalRandom

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.internal.module
 *
 * @author InkerXoe
 * @since 2024/2/19 下午10:33
 */
object DropModule {

    fun runNormalDrop(event: PlayerDeathEvent) {
        val player = event.entity.player!!
        val inventory = player.inventory
        val location = player.location

        if (!event.keepInventory) {
            inventory.forEach { item ->
                location.world!!.dropItem(location, item)
                item?.type = Material.AIR
            }
        }
    }
    fun checkDropInv(config: Map<String, Any?>, player: Player): List<Int> {
        if (!config["enable"].cbool) return emptyList()
        val playerInventoryItems = ToolsUtil.getInvItem(player.inventory)
        val playerInventoryIndices = playerInventoryItems.map { it.first }.toSet()
        val protectedSlot: MutableSet<Int> = mutableSetOf()
        val enforcedSlot: MutableSet<Int> = mutableSetOf()

        // 处理配置中的protected和enforced部分
        fun processConfigSection(section: Map<String, Any?>, slotSet: MutableSet<Int>) {
            section["info"]?.asList()?.takeIf { section["enable"].cbool }?.forEach { conf ->
                val (key, value) = conf.split("<->")
                slotSet += when (key) {
                    "slot" -> value.split('|').map(String::cint).filter { it in playerInventoryIndices }
                    "material" -> playerInventoryItems.filter { (_, item) -> item.type == value.toMaterial() }.map { (index, _) -> index }
                    "lore" -> playerInventoryItems.filter {
                        (_, item) -> item.hasLore(value) }.map { (index, _) -> index
                        }
                    "nbt" -> {
                        val (k, v) = value.split(":")
                        playerInventoryItems.filter { (_, item) -> item.getItemTag()[k] == ItemTagData(v) }.map { (index, _) -> index }
                    }
                    else -> emptyList()
                }
            }
        }

        // 初始化protectedSlot和enforcedSlot
        processConfigSection(config["protected"].asMap(), protectedSlot)
        processConfigSection(config["enforced"].asMap(), enforcedSlot)

        debug {
            debug("处理完的保护位: $protectedSlot")
            debug("处理完的必掉位: $enforcedSlot")
        }

        // 根据优先级处理重合部分
        if (config["priority"].toString() == "protected") {
            enforcedSlot.removeAll(protectedSlot)
        } else {
            protectedSlot.removeAll(enforcedSlot)
        }

        val finalDropSlots: MutableSet<Int> = mutableSetOf()

        // 根据drop type确定掉落的slots
        config["type"]?.toString()?.let { dropType ->
            val setting = config["info"].toString()
            val remainingInventory = playerInventoryItems.filterNot { (index, _) -> index in protectedSlot || index in enforcedSlot }

            finalDropSlots += when (dropType) {
                "percentage" -> {
                    val percent = setting.parsePercent()
                    remainingInventory.shuffled().take((remainingInventory.size * percent).toInt()).map { it.first }.toSet()
                }
                "range" -> {
                    val (min, max) = setting.split("..").map(String::toInt)
                    remainingInventory.shuffled().take((min..max).random()).map { it.first }.toSet()
                }
                "slot" -> setting.split('|').map(String::toInt).filter { it in playerInventoryIndices }.toSet()
                "material" -> remainingInventory.filter { (_, item) -> item.type.toString() in setting.split('|') }.map { it.first }.toSet()
                "lore" -> remainingInventory.filter { (_, item) -> item.hasLore(setting) }.map { it.first }.toSet()
                "nbt" -> {
                    val (k, v) = setting.split(":")
                    remainingInventory.filter { (_, item) -> item.getItemTag()[k] == ItemTagData(v) }.map { it.first }.toSet()
                }
                "all" -> remainingInventory.map { it.first }.toSet()
                "none" -> emptySet()
                else -> throw IllegalArgumentException("Unknown drop type: $dropType")
            }
        }

        finalDropSlots += enforcedSlot
        return finalDropSlots
            .sortedDescending()
    }

    fun checkDropExp(config: Map<String, Any?>, player: Player): Int {
        if (!config["enable"].cbool) return 0
        val dropType = config["type"].toString()
        val setting = config["info"].toString()
        val didnt = config["didnt"].cint
        var result: Int
        val level = player.level
        if (didnt >= level) return 0
        when (dropType) {
            "percentage" -> {
                val info = setting.removeSuffix("%").cfloat
                val per = info / 100.0
                result = (level * per).cint
            }
            "range" -> {
                val (setLeft, setRight) = setting.split("..").map { it.cint }
                result = ThreadLocalRandom.current().nextInt(setLeft, setRight + 1)
            }
            "amount" -> {
                result = setting.cint
            }
            "none" -> {
                result = 0
            }
            "all" -> {
                result = level
            }
            else -> throw IllegalArgumentException("Invalid drop type")
        }
        if (result > level) result = level
        debug("获取最终玩家掉落Exp -> $result")
        return  result
    }
}