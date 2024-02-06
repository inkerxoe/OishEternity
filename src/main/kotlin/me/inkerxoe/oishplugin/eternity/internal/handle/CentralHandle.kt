package me.inkerxoe.oishplugin.eternity.internal.handle

import me.inkerxoe.oishplugin.eternity.internal.manager.ActionManager
import me.inkerxoe.oishplugin.eternity.internal.manager.ConfigManager
import me.inkerxoe.oishplugin.eternity.internal.module.ConfigModule
import me.inkerxoe.oishplugin.eternity.internal.module.RegionModule
import me.inkerxoe.oishplugin.eternity.internal.module.ScriptModule
import me.inkerxoe.oishplugin.eternity.internal.module.SelectModule
import me.inkerxoe.oishplugin.eternity.utils.ToolsUtil
import me.inkerxoe.oishplugin.eternity.utils.ToolsUtil.toResult
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common5.cbool
import taboolib.common5.clong
import taboolib.module.configuration.util.asMap
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
        val sender = player ?: event!!.entity.player

        // check handle
        val map = ConfigManager.map.filter { (key, value) ->
            ToolsUtil.debug("-----=Check <-> $key <-> Start=-----")
            // 总配置
            val disposition = value["disposition"].asMap()
            // 判断逻辑配置
            val check = disposition["check"].asMap()

            // pre-action
            val preAction = check["pre_action"].asMap()
            val typ = preAction["type"].toString()
            ToolsUtil.debug("set pre_action.type to $preAction")
            val script = preAction["script"].toString()
            ToolsUtil.debug("set pre_action.script to $script")
            // 方便添加参数 pre_action
            val args = hashMapOf<Any, Any>()
            var result = ActionManager.runAction(typ, sender, script, event, type, args)
            ToolsUtil.debug("pre-action result -> $result")
            if (!result) return@filter false
            // pre-action执行返回true 继续执行后续判断

            // select 配置
            val select = check["select"].asMap()
            if (!SelectModule.handle(select, sender!!)) return@filter false

            // region 配置
            val region = check["region"].asMap()
            if (!RegionModule.handle(region, sender)) return@filter false

            ToolsUtil.debug("-----=Check <-> $key <-> End=-----")
            return@filter true
        }
        val key = map.keys.first()
        ToolsUtil.debug("Check Config -> $key")

        // action handle
        val config = map[key]!!
        val desc = config.get("desc").toString()
        ToolsUtil.debug("Config description -> $desc")
        ToolsUtil.debug("-----=Action <-> $key <-> Start=-----")
        // 获取总配置
        val disposition = config["disposition"].asMap()
        // 获取 action 配置
        val action = disposition["action"].asMap()

        // main_action
        val mainAction = action["main_action"].asMap()
        val typ = mainAction["type"].toString()
        ToolsUtil.debug("set main_action.type to $mainAction")
        val script = mainAction["script"].toString()
        ToolsUtil.debug("set main_action.script to $script")
        // 方便添加参数 main_action
        val args = hashMapOf<Any, Any>()
        var result = ActionManager.runAction(typ, sender, script, event, type, args)
        ToolsUtil.debug("main_action result -> $result")
        if (!result) return

        // drop









        ToolsUtil.debug("-----=Action <-> $key <-> End=-----")
        ToolsUtil.debug("插件逻辑执行完毕! 耗时 &6${ToolsUtil.timing(startTime)} &fms")
    }
}