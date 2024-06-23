package me.inkerxoe.oishplugin.eternity.internal.handle

import me.inkerxoe.oishplugin.eternity.OishEternity.bukkitScheduler
import me.inkerxoe.oishplugin.eternity.OishEternity.plugin
import me.inkerxoe.oishplugin.eternity.internal.manager.ActionManager
import me.inkerxoe.oishplugin.eternity.internal.module.DropModule
import me.inkerxoe.oishplugin.eternity.internal.module.RelicModule
import me.inkerxoe.oishplugin.eternity.internal.module.SpawnModule
import me.inkerxoe.oishplugin.eternity.utils.ToolsUtil
import me.inkerxoe.oishplugin.eternity.utils.ToolsUtil.debug
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import taboolib.common5.cbool
import taboolib.common5.cint
import taboolib.common5.clong
import taboolib.module.configuration.util.asMap
import taboolib.platform.util.killer

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.internal.handle
 *
 * @author InkerXoe
 * @since 2024/2/19 上午10:30
 */
object ActionHandle {
    fun action(event: PlayerDeathEvent, config: HashMap<String, Any>) {
        debug("-----=Action <-> ${config.keys.first()} <-> Start=-----")
        val key = config.keys.first()
        val set = config[key].asMap()

        val player = event.entity.player!!

        // 总配置
        val disposition = set["disposition"].asMap()
        // 动作逻辑配置
        val action = disposition["action"].asMap()

        // 获取 drop 配置
        val drop = action["drop"].asMap()
        // item
        val ite = drop["item"].asMap()
        // exp
        val exp = drop["exp"].asMap()
        // 获取玩家掉落格
        val dropInv = DropModule.checkDropInv(ite, player)

        val inv = player.inventory

        val stack = dropInv.map { inv.getItem(it) }

        val itemStackInv: ArrayList<ItemStack> = arrayListOf()
        debug("剔除玩家背包物品 -> Run")
        debug("dropInv -> $dropInv")
        dropInv.forEach { slot ->
            val item = player.inventory.getItem(slot)
            itemStackInv.add(item!!)
            player.inventory.setItem(slot, null)
        }
        debug("剔除玩家背包物品 -> End")

        //处理玩家掉落经验
        val dropExp = DropModule.checkDropExp(exp, player)
        debug("获取到的应掉等级 -> $dropExp")
        val resultExp = player.level - dropExp
        debug("玩家处理后等级 -> $resultExp")
        player.level = resultExp

        //处理玩家遗物
        //获取relic
        val relic = action["relic"].asMap()
        if (dropInv.isNotEmpty())
            RelicModule.runRelic(relic, player, stack)

        // 处理Redeem TODO

        // 处理 Spawn
        val spawn = action["spawn"].asMap()
        debug("spawn -> $spawn")
        val loc = SpawnModule.checkLocation(event, spawn)
        if (spawn["enable"].cbool) {
            debug("最终获取的Location -> $loc")
            val auto = spawn["auto_spawn"].asMap()
            val enable = auto["enable"].cbool
            val delay = auto["delay"].clong
            if (enable && loc != null) {
                bukkitScheduler.runTaskLater(plugin, Runnable {
                    player.spigot().respawn()
                    player.teleport(loc)
                }, delay)
            } else if (loc != null) {
                player.spigot().respawn()
                player.teleport(loc)
            }
        }

        // post-action 开始
        val preAction = action["pre_action"].asMap()
        val preEnable = preAction["enable"].cbool
        // 方便后续添加参数
        val args = hashMapOf<Any, Any>()
        args["player"] = event.entity.player!!
        args["event"] = event
        args["deathWorld"] = event.entity.world
        args["level"] = event.entity.level
        args["location"] = event.entity.location
        args["killer"] = event.killer?: "null"
        if (preEnable)
            ActionManager.runActionHandle(preAction, args)
        // post-action 结束
        debug("-----=Action <-> ${config.keys.first()} <-> End=-----")
    }
}