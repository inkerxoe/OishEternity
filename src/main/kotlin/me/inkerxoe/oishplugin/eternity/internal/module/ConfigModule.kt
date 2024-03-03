package me.inkerxoe.oishplugin.eternity.internal.module

import me.inkerxoe.oishplugin.eternity.OishEternity
import me.inkerxoe.oishplugin.eternity.utils.ConfigUtil
import me.inkerxoe.oishplugin.eternity.utils.ConfigUtil.saveResourceNotWarn
import me.inkerxoe.oishplugin.eternity.utils.ToolsUtil
import org.bukkit.configuration.ConfigurationSection
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import java.io.File

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.internal.module
 *
 * @author InkerXoe
 * @since 2024/2/4 16:27
 */
object ConfigModule {
    val options_debug
        get() = OishEternity.setting.getBoolean("options.debug", false)
    val options_identifiers_script_KETHER
        get() = OishEternity.setting.getString("options.identifiers.script.kether")
    val options_identifiers_script_JAVASCRIPT
        get() = OishEternity.setting.getString("options.identifiers.script.javascript")
    val options_identifiers_select_perm_type_all
        get() = OishEternity.setting.getString("options.identifiers.select.perm.type.all")
    val options_identifiers_select_perm_type_portion
        get() = OishEternity.setting.getString("options.identifiers.select.perm.type.portion")
    val options_identifiers_region_territory_type_adapter_residence
        get() = OishEternity.setting.getString("options.identifiers.region.territory.adapter.residence")
    val options_identifiers_region_territory_type_adapter_grief_defender
        get() = OishEternity.setting.getString("options.identifiers.region.territory.adapter.grief-defender")
    val options_identifiers_region_territory_type_adapter_world_guard
        get() = OishEternity.setting.getString("options.identifiers.region.territory.adapter.world_guard")
    val options_death_info
        get() = OishEternity.setting.getBoolean("options.death_info", true)





    @Awake(LifeCycle.INIT)
    fun saveResource() {
        if (ConfigUtil.getFileOrNull("workspace") == null) {
            OishEternity.plugin.saveResourceNotWarn("workspace${File.separator}example.yml")
        }
        if (ConfigUtil.getFileOrNull("script") == null) {
            OishEternity.plugin.saveResourceNotWarn("script${File.separator}example.js")
        }
    }

    /**
     * 重载配置管理器
     */
    fun reload() {
        OishEternity.plugin.reloadConfig()
        saveResource()
    }
}