package me.inkerxoe.oishplugin.eternity.internal.module

import me.inkerxoe.oishplugin.eternity.utils.ToolsUtil
import me.inkerxoe.oishplugin.eternity.utils.ToolsUtil.debug
import me.inkerxoe.oishplugin.eternity.utils.ToolsUtil.parsePercent
import me.inkerxoe.oishplugin.eternity.utils.ToolsUtil.toMaterial
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
        inventory.forEach { item ->
            item.type = Material.AIR
            location.world!!.dropItem(location, item)
        }
    }
    fun checkDropInv(config: Map<String, Any?>, player: Player): List<Int> {
        if (!config["enable"].cbool) return arrayListOf()
        val playerInventory = ToolsUtil.getInvItem(player.inventory)
        val protectedSlot: ArrayList<Int> = arrayListOf()
        // 获取保护格配置
        val protected = config["protected"].asMap()
        val protectedConf = protected["info"]?.asList()
        if (protected["enable"].cbool) {
            protectedConf?.forEach { conf ->
                val (key, value) = conf.split("<->")
                when (key) {
                    "slot" -> {
                        protectedSlot += value.split('|').map(String::cint)
                            .filter { playerInventory.map { i -> i.first }.contains(it) }
                    }
                    "material" -> {
                        val material = value.toMaterial()
                        protectedSlot += playerInventory
                            .filter { (_, item) -> item.type == material }
                            .map { (index, _) -> index }
                    }
                    "lore" -> {
                        protectedSlot += playerInventory
                            .filter { (_, item) -> item.hasLore(value) }
                            .map { (index, _) -> index }
                    }
                    "nbt" -> {
                        val (k, v) = value.split(":")
                        protectedSlot += playerInventory
                            .filter { (_, item) -> item.getItemTag()[k] == ItemTagData(v) }
                            .map { (index, _) -> index }
                    }
                }
            }
            val result = protectedSlot.distinct().sortedDescending()
            protectedSlot.clear()
            protectedSlot.addAll(result)
        }
        debug("获取到的保护格Slot列表 -> $protectedSlot")

        // 强制掉落逻辑
        val enforcedSlot: ArrayList<Int> = arrayListOf()
        val enforced = config["enforced"].asMap()
        val enforcedConf = protected["info"]?.asList()
        if (enforced["enable"].cbool) {
            enforcedConf?.forEach { conf ->
                val (key, value) = conf.split("<->")
                when (key) {
                    "slot" -> {
                        enforcedSlot += value.split('|').map(String::cint)
                            .filter { playerInventory.map { i -> i.first }.contains(it) }
                    }
                    "material" -> {
                        val material = value.toMaterial()
                        enforcedSlot += playerInventory
                            .filter { (_, item) -> item.type == material }
                            .map { (index, _) -> index }
                    }
                    "lore" -> {
                        enforcedSlot += playerInventory
                            .filter { (_, item) -> item.hasLore(value) }
                            .map { (index, _) -> index }
                    }
                    "nbt" -> {
                        val (k, v) = value.split(":")
                        enforcedSlot += playerInventory
                            .filter { (_, item) -> item.getItemTag()[k] == ItemTagData(v) }
                            .map { (index, _) -> index }
                    }
                }
            }
            val result = enforcedSlot.distinct().sortedDescending()
            enforcedSlot.clear()
            enforcedSlot.addAll(result)
        }
        debug("获取到的强制掉落Slot列表 -> $enforcedSlot")


        // 处理玩家Inventory
        val newPreInventory = playerInventory.filterNot { (index, _) -> protectedSlot.contains(index) }
        val newInventory = newPreInventory.filterNot { (index, _) -> enforcedSlot.contains(index) }
        debug("获得处理后的玩家Slot列表 -> $newInventory")

        // 处理玩家Drop列表
        val dropType = config["type"].toString()
        val setting = config["info"].toString()
        val dropSlot: ArrayList<Int> = arrayListOf()

        when (dropType) {
            "percentage" -> {
                // 百分比 -> 50%
                val percent = setting.parsePercent()
                val slotsToTake = (newInventory.size * percent).cint
                dropSlot += newInventory.shuffled().take(slotsToTake).map { it.first }
            }
            "range" -> {
                // 范围 -> 1..2
                val (setLeft, setRight) = setting.toString().split("..").map { it.cint }
                val result = ThreadLocalRandom.current().nextInt(setLeft, setRight + 1)
                dropSlot += newInventory.shuffled().take(result).map { it.first }
            }
            "slot" -> {
                // 指定格 -> 1|2|3
                dropSlot += setting.split('|').map(String::cint).mapNotNull { slotIndex ->
                    newInventory.find { it.first == slotIndex.cint }?.first
                }
            }
            "amount" -> {
                // 指定数
                dropSlot += newInventory.shuffled().take(setting.cint).map { it.first }

            }
            "material" -> {
                // 指定Material
                val material = setting.split('|').map { it.toMaterial() }
                dropSlot += newInventory
                    .filter { (_, item) -> item.type in material }
                    .map { (index, _) -> index }
            }
            "lore" -> {
                // 指定Lore
                dropSlot += newInventory
                    .filter { (_, item) -> item.hasLore(setting) }
                    .map { (index, _) -> index }
            }
            "nbt" -> {
                // 指定Nbt
                val (k, v) = setting.split(":")
                dropSlot += newInventory
                    .filter { (_, item) -> item.getItemTag()[k] == ItemTagData(v) }
                    .map { (index, _) -> index }
            }
            "all" -> {
                // 全掉落
                dropSlot += newInventory.map { it.first }
            }
            "none" -> {
                // 不掉落
                // 宝 你在看什么？
                dropSlot += arrayListOf()
            }
            else -> throw IllegalArgumentException("Invalid drop type")
        }
        val result = dropSlot.distinct().sortedDescending()
        dropSlot.clear()
        dropSlot.addAll(result)
        dropSlot.addAll(enforcedSlot)
        debug("获取最终玩家掉落Slot列表 -> $dropSlot")
        return dropSlot
    }

    fun checkDropExp(config: Map<String, Any?>, player: Player): Int {
        if (config["enable"].cbool) return 0
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