package me.inkerxoe.oishplugin.eternity.utils

import me.inkerxoe.oishplugin.eternity.internal.manager.ConfigManager
import me.inkerxoe.oishplugin.eternity.internal.module.ConfigModule
import org.bukkit.GameRule
import org.bukkit.Material
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import taboolib.common.platform.function.console
import taboolib.common5.Coerce
import taboolib.module.lang.sendLang
import taboolib.platform.util.isNotAir

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.utils
 *
 * @author InkerXoe
 * @since 2024/2/3 15:38
 */
object ToolsUtil {
    fun printLogo() {
        console().sendMessage("§b   ____  _     __   ______               _ __      ")
        console().sendMessage("§b  / __ \\(_)__ / /  / __/ /____ _______  (_) /___ __")
        console().sendMessage("§b / /_/ / (_-</ _ \\/ _// __/ -_) __/ _ \\/ / __/ // /")
        console().sendMessage("§b \\____/_/___/_//_/___/\\__/\\__/_/ /_//_/_/\\__/\\_, / ")
        console().sendMessage("§b                                            /___/  ")
    }

    fun debug(text: String) {
        if (ConfigModule.options_debug) {
            console().sendLang("Plugin-Debug", text)
        }
    }

    fun debug(a: () -> Unit) {
        if (ConfigModule.options_debug) {
            a.invoke()
        }
    }

    fun timing(): Long {
        return System.nanoTime()
    }

    fun timing(start: Long): Double {
        return Coerce.format((System.nanoTime() - start).div(1000000.0))
    }

    fun Any.toResult(): Boolean {
        return when (this) {
            is Boolean -> { this }
            is String -> {
                when (this) {
                    "true" -> true
                    "false" -> false
                }
                true
            }
            else -> { true }
        }
    }

    fun setKeep(event: PlayerDeathEvent, keep: Boolean) {
        try {
            event.entity.world.setGameRule(GameRule.KEEP_INVENTORY, keep)
        } catch (error: NoClassDefFoundError) {
            debug("GameRule -> ${error.cause}")
            debug("手动设置中...")
            event.keepInventory = keep
            debug("keepInventory -> ${event.keepInventory}")
            event.keepLevel = keep
            debug("keepLevel -> ${event.keepLevel}")
        }
    }

    fun getInvItem(inventory: PlayerInventory): List<Pair<Int, ItemStack>> {
        return inventory.withIndex()
            .filter { (_, itemStack) -> itemStack.isNotAir() }
            .map { (index, itemStack) -> index to itemStack }
    }

    fun String.toMaterial(): Material {
        return Material.getMaterial(this) ?: throw IllegalArgumentException("Invalid material name: $this")
    }

    fun String.parsePercent(): Float {
        return this.removeSuffix("%").toFloat() / 100.0f
    }
}