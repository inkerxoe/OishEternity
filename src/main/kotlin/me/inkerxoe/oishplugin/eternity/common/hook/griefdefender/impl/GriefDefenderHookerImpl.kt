package me.inkerxoe.oishplugin.eternity.common.hook.griefdefender.impl

import com.griefdefender.api.GriefDefender
import me.inkerxoe.oishplugin.eternity.common.hook.griefdefender.GriefDefenderHooker
import org.bukkit.OfflinePlayer

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.common.hook.griefdefender.impl
 *
 * @author InkerXoe
 * @since 2024/2/6 10:45
 */
/**
 * GriefDefender附属领地挂钩
 *
 * @constructor 启用GriefDefender附属领地挂钩
 */
class GriefDefenderHookerImpl : GriefDefenderHooker() {
    override fun getLocation(player: OfflinePlayer): String? {
        val claim = GriefDefender.getCore().getClaimAt(player.player!!.location)
        return claim?.displayName
    }
}