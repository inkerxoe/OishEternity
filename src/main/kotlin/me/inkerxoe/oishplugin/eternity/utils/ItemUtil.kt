import me.inkerxoe.oishplugin.eternity.OishEternity
import org.bukkit.Location
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

object ItemUtil {

    fun dropItems(
        plugin: Plugin, // 你的插件实例
        itemsToDrop: List<ItemStack>,
        dropLocation: Location,
        offsetXString: String,
        offsetYString: String,
        angleType: String,
        delayTicksBetweenDrops: Long // 每次物品掉落之间的延迟（以ticks为单位）
    ) {
        itemsToDrop.forEachIndexed { index, itemStack ->
            object : BukkitRunnable() {
                override fun run() {
                    val world = dropLocation.world ?: return

                    // 计算 offsetX 和 offsetY
                    val offsetX = calculateOffset(offsetXString)
                    val offsetY = calculateOffset(offsetYString)

                    // 掉落物品
                    val droppedItem: Item = world.dropItem(dropLocation, itemStack)

                    // 根据 angleType 计算出发射角度
                    val angle = when (angleType) {
                        "round" -> 2 * Math.PI * index / itemsToDrop.size
                        "random" -> Random.nextDouble() * 2 * Math.PI
                        else -> 0.0
                    }

                    // 计算发射向量
                    val vector = Vector(cos(angle) * offsetX, offsetY, sin(angle) * offsetX)

                    // 设置物品的速度
                    droppedItem.velocity = vector
                }
            }.runTaskLater(plugin, index * delayTicksBetweenDrops)
        }
    }

    fun calculateOffset(offsetString: String): Double {
        return if (offsetString.contains("-")) {
            val (min, max) = offsetString.split("-").map { it.toDouble() }
            Random.nextDouble(min, max)
        } else {
            offsetString.toDoubleOrNull() ?: 0.1
        }
    }
}