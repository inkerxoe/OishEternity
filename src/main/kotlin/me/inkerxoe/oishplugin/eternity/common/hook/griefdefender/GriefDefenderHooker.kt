package me.inkerxoe.oishplugin.eternity.common.hook.griefdefender

import org.bukkit.OfflinePlayer

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.common.hook.griefdefender
 *
 * @author InkerXoe
 * @since 2024/2/6 10:08
 */
/**
 * GriefDefender附属领地挂钩
 */
abstract class GriefDefenderHooker {
    /**
     * 获取玩家所在领地名
     *
     * @param player 待操作玩家
     */
    abstract fun getLocation(player: OfflinePlayer): String?
}