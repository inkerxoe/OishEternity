package me.inkerxoe.oishplugin.eternity.internal.module

import me.inkerxoe.oishplugin.eternity.OishEternity
import me.inkerxoe.oishplugin.eternity.utils.ConfigUtils
import me.inkerxoe.oishplugin.eternity.utils.ConfigUtils.getTop
import me.inkerxoe.oishplugin.eternity.utils.ToolsUtil
import org.bukkit.configuration.ConfigurationSection

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.internal.module
 *
 * @author InkerXoe
 * @since 2024/2/4 16:27
 */
object ConfigModule {
    val map: MutableMap<String,ConfigurationSection> = hashMapOf()
    fun loadConfigs(){
        val yml = ConfigUtils.getAllFiles(OishEternity.plugin,"workspace").filter {
            it.endsWith(".yml")
        }
        yml.forEach { file ->
            file.getTop().filter {
                it is ConfigurationSection
            }.forEach {
                it as ConfigurationSection
                map[it.name] = it
                ToolsUtil.debug("ConfigLoading: $it")
            }
        }
    }

    fun reload(){
        map.clear()
        loadConfigs()
    }
}