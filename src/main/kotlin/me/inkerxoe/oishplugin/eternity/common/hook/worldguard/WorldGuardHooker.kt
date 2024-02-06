package me.inkerxoe.oishplugin.eternity.common.hook.worldguard

import org.bukkit.OfflinePlayer

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.common.hook.worldguard
 *
 * @author InkerXoe
 * @since 2024/2/6 10:10
 */
/**
 * WorldGuard附属领地挂钩
 */
abstract class WorldGuardHooker {
    /**
     * 获取玩家所在领地名
     *
     * @param player 待操作玩家
     */
    abstract fun getLocation(player: OfflinePlayer): String?
}