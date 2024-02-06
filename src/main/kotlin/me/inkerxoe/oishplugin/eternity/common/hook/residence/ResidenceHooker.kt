package me.inkerxoe.oishplugin.eternity.common.hook.residence

import org.bukkit.OfflinePlayer

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.common.hook.residence
 *
 * @author InkerXoe
 * @since 2024/2/6 10:10
 */
/**
 * Residence附属领地挂钩
 */
abstract class ResidenceHooker {
    /**
     * 获取玩家所在领地名
     *
     * @param player 待操作玩家
     */
    abstract fun getLocation(player: OfflinePlayer): String?
}
