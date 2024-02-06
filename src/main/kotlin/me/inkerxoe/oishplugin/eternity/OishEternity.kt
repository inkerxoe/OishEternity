package me.inkerxoe.oishplugin.eternity


import org.bukkit.Bukkit
import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import taboolib.platform.BukkitPlugin

object OishEternity : Plugin() {
    val plugin by lazy { BukkitPlugin.getInstance() }

    val bukkitScheduler by lazy { Bukkit.getScheduler() }

    val hookerGriefDefender by lazy { Bukkit.getPluginManager().isPluginEnabled("GriefDefender") }

    val hookerResidence by lazy { Bukkit.getPluginManager().isPluginEnabled("Residence") }

    val hookerWorldGuard by lazy { Bukkit.getPluginManager().isPluginEnabled("WorldGuard") }

    @Config("setting.yml", autoReload = false)
    lateinit var setting: ConfigFile
}