package me.inkerxoe.oishplugin.eternity.common.hook.worldguard.impl

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldguard.WorldGuard
import me.inkerxoe.oishplugin.eternity.common.hook.worldguard.WorldGuardHooker
import org.bukkit.OfflinePlayer

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.common.hook.worldguard.impl
 *
 * @author InkerXoe
 * @since 2024/2/6 10:46
 */
/**
 * WorldGuard附属领地挂钩
 *
 * @constructor 启用Residence附属领地挂钩
 */
class WorldGuardHookerImpl : WorldGuardHooker() {
    override fun getLocation(player: OfflinePlayer): String? {
        val worldGuardManager = WorldGuard.getInstance().platform.regionContainer.get(BukkitAdapter.adapt(player.player!!.world))
        val x = player.player!!.location.x
        val y = player.player!!.location.y
        val z = player.player!!.location.z
        val regions = worldGuardManager?.getApplicableRegions(BlockVector3.at(x,y,z))
        val region = regions?.firstOrNull()
        return region?.id
    }
}