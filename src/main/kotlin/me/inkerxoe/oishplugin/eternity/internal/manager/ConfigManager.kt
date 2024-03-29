package me.inkerxoe.oishplugin.eternity.internal.manager

import me.inkerxoe.oishplugin.eternity.OishEternity.plugin
import me.inkerxoe.oishplugin.eternity.OishEternity
import me.inkerxoe.oishplugin.eternity.internal.module.ConfigModule
import me.inkerxoe.oishplugin.eternity.utils.ConfigUtil
import me.inkerxoe.oishplugin.eternity.utils.ConfigUtil.coverWith
import me.inkerxoe.oishplugin.eternity.utils.ConfigUtil.getAllFiles
import me.inkerxoe.oishplugin.eternity.utils.ConfigUtil.getConfigSections
import me.inkerxoe.oishplugin.eternity.utils.ConfigUtil.getContents
import me.inkerxoe.oishplugin.eternity.utils.ConfigUtil.toMap
import me.inkerxoe.oishplugin.eternity.utils.FileUtil
import me.inkerxoe.oishplugin.eternity.utils.ToolsUtil
import org.bukkit.configuration.ConfigurationSection
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import taboolib.common.platform.Platform
import taboolib.common.platform.function.console
import taboolib.module.configuration.util.asMap
import taboolib.module.lang.sendLang
import taboolib.module.metrics.Metrics
import java.io.File
import java.io.InputStreamReader

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.internal.manager
 *
 * @author InkerXoe
 * @since 2024/2/4 14:47
 */
object ConfigManager {

    val map: MutableMap<String, HashMap<String, Any>> = hashMapOf()

    @Awake(LifeCycle.ENABLE)
    private fun loadConfigs(){
        val start = ToolsUtil.timing()
        ToolsUtil.debug("loadConfig...")
        val yml = getAllFiles("workspace").filter {
            it.name.endsWith(".yml")
        }
        yml.forEach { file ->
            var key = ""
            file.getContents().filterIsInstance<ConfigurationSection>().forEach {
                key = it.name
            }
            val configSection = file.getConfigSections()
            val section: ConfigurationSection = configSection.first()
            for (sec in configSection) {
                section.coverWith(sec)
            }
            val secMap = section.toMap()
            map[key] = secMap
            ToolsUtil.debug("loadConfig[$key] -> ${map[key]}")
        }
        console().sendLang("Plugin-Config-Load", map.size, ToolsUtil.timing(start))
    }

    fun reload(){
        map.clear()
        loadConfigs()
    }
}