package me.inkerxoe.oishplugin.eternity.internal.handle

import me.inkerxoe.oishplugin.eternity.internal.manager.ActionManager
import me.inkerxoe.oishplugin.eternity.internal.manager.ConfigManager
import me.inkerxoe.oishplugin.eternity.internal.module.RegionModule
import me.inkerxoe.oishplugin.eternity.internal.module.SelectModule
import me.inkerxoe.oishplugin.eternity.utils.ToolsUtil
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common5.cbool
import taboolib.common5.cint
import taboolib.module.configuration.util.asMap
import taboolib.platform.util.killer

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.internal.handle
 *
 * @author InkerXoe
 * @since 2024/2/19 上午10:29
 */
object CheckHandle {
    fun check(event: PlayerDeathEvent): HashMap<String, Any> {
        //debug
        ToolsUtil.debug("<------------>")
        ToolsUtil.debug("监听到玩家${event.entity.player!!.name}死亡 开始处理插件逻辑...")
        ToolsUtil.debug("player -> ${event.entity.player}")
        ToolsUtil.debug("level -> ${event.entity.level}")
        ToolsUtil.debug("deathWorld -> ${event.entity.world}")
        ToolsUtil.debug("location -> ${event.entity.location}")
        ToolsUtil.debug("killer -> ${event.killer}")
        ToolsUtil.debug("event -> $event")

        // 返回获取到的对应配置Map
        return checkConfig(event)
    }

    private fun checkConfig(event: PlayerDeathEvent): HashMap<String, Any> {

        // 方便后续添加参数
        val args = hashMapOf<Any, Any>()
        args["player"] = event.entity.player!!
        args["event"] = event
        args["deathWorld"] = event.entity.world
        args["level"] = event.entity.level
        args["location"] = event.entity.location
        args["killer"] = event.killer?: "null"

        val map = ConfigManager.map.filter { (key, value) ->
            ToolsUtil.debug("-----=Check <-> $key <-> Start=-----")
            // 总配置
            val disposition = value["disposition"].asMap()
            // 判断逻辑配置
            val check = disposition["check"].asMap()

            // pre-action 开始
            val preAction = check["pre_action"].asMap()
            val preEnable = preAction["enable"].cbool
            if (preEnable) {
                val result = ActionManager.runActionHandle(preAction, args)
                ToolsUtil.debug("preAction -> $result")
                if (!result) return@filter false
            }
            // pre-action 结束
            val player = event.entity.player

            // select config
            val select = check["select"].asMap()
            val selectResult = SelectModule.handle(select, player!!)
            ToolsUtil.debug("select -> $selectResult")
            if (!selectResult) return@filter false

            // region config
            val region = check["region"].asMap()
            val regionResult = RegionModule.handle(region, player)
            ToolsUtil.debug("region -> $regionResult")
            if (!regionResult) return@filter false

            // main-action 开始
            val mainAction = check["pre_action"].asMap()
            val mainEnable = preAction["enable"].cbool
            if (mainEnable) {
                val result = ActionManager.runActionHandle(mainAction, args)
                ToolsUtil.debug("mainAction -> $result")
                if (!result) return@filter false
            }
            // main-action 结束
            return@filter true
        }
        ToolsUtil.debug("原始列表 -> $map")
        if (map.isEmpty()) return  HashMap()

        val weightList = map.map { (_, value) -> value["weight"].cint }
        val maxWeight = weightList.max()
        val conf = map.filter { (_,value) -> value["weight"] == maxWeight }
        val confKey = conf.keys.first()
        val checkedConfig = conf[confKey]!!
        ToolsUtil.debug("权重处理后列表 -> $checkedConfig")
        ToolsUtil.debug("-----=Check <-> ${checkedConfig.keys} <-> End=-----")
        return checkedConfig
    }
}