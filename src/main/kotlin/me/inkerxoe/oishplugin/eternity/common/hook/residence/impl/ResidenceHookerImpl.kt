package me.inkerxoe.oishplugin.eternity.common.hook.residence.impl

import com.bekvon.bukkit.residence.Residence
import com.bekvon.bukkit.residence.protection.ClaimedResidence
import me.inkerxoe.oishplugin.eternity.common.hook.residence.ResidenceHooker
import org.bukkit.OfflinePlayer

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.common.hook.residence.impl
 *
 * @author InkerXoe
 * @since 2024/2/6 10:45
 */
/**
 * Residence附属领地挂钩
 *
 * @constructor 启用Residence附属领地挂钩
 */
class ResidenceHookerImpl : ResidenceHooker() {
    override fun getLocation(player: OfflinePlayer): String? {
        val resManager = Residence.getInstance().residenceManager
        val res: ClaimedResidence? = resManager.getByLoc(player.player!!.location)
        return res?.name
    }
}