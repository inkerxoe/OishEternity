package me.inkerxoe.oishplugin.eternity.utils

import me.inkerxoe.oishplugin.eternity.OishEternity
import me.inkerxoe.oishplugin.eternity.OishEternity.plugin
import me.inkerxoe.oishplugin.eternity.utils.FileUtil.createDirectory
import me.inkerxoe.oishplugin.eternity.utils.FileUtil.createFile
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.utils
 *
 * @author InkerXoe
 * @since 2024/2/4 10:04
 */
/**
 * 配置文件相关工具类
 */
object ConfigUtil {
    /**
     * 获取文件夹内所有文件
     *
     * @param dir 待获取文件夹
     * @return 文件夹内所有文件
     */
    @JvmStatic
    fun getAllFiles(dir: File): ArrayList<File> {
        val list = ArrayList<File>()
        val files = dir.listFiles() ?: arrayOf<File>()
        for (file: File in files) {
            if (file.isDirectory) {
                list.addAll(getAllFiles(file))
            } else {
                list.add(file)
            }
        }
        return list
    }

    /**
     * 获取文件夹内所有文件
     *
     * @param dir 待获取文件夹路径
     * @return 文件夹内所有文件
     */
    @JvmStatic
    fun getAllFiles(dir: String): ArrayList<File> {
        return getAllFiles(File(plugin.dataFolder, File.separator + dir))
    }

    /**
     * 获取文件夹内文件
     *
     * @param file 待获取文件路径
     * @return 对应文件
     */
    @JvmStatic
    fun getFile(file: String): File {
        return File(plugin.dataFolder, File.separator + file)
    }

    /**
     * 获取文件夹内文件(不存在时返回null)
     *
     * @param file 待获取文件路径
     * @return 对应文件
     */
    @JvmStatic
    fun getFileOrNull(file: String): File? {
        return File(plugin.dataFolder, File.separator + file).let {
            if (!it.exists()) null
            else it
        }
    }

    /**
     * 获取文件夹内文件(不存在时创建文件)
     *
     * @param file 待获取文件路径
     * @return 对应文件
     */
    @JvmStatic
    fun getFileOrCreate(file: String): File {
        return File(plugin.dataFolder, File.separator + file).createFile()
    }

    /**
     * 获取文件夹内所有文件
     *
     * @param plugin 待获取文件夹归属插件
     * @param dir 待获取文件夹路径
     * @return 文件夹内所有文件
     */
    @JvmStatic
    fun getAllFiles(plugin: Plugin, dir: String): ArrayList<File> {
        return getAllFiles(File(plugin.dataFolder, File.separator + dir))
    }

    /**
     * 获取文件夹内所有文件
     *
     * @param plugin 待获取文件夹归属插件
     * @param dir 待获取文件夹路径
     * @return 文件夹内所有文件
     */
    @JvmStatic
    fun getAllFiles(plugin: String, dir: String): ArrayList<File> {
        return getAllFiles(
            File(
                File(OishEternity.plugin.dataFolder.parent, File.separator + plugin),
                File.separator + dir
            )
        )
    }

    /**
     * 获取文件夹内文件
     *
     * @param plugin 待获取文件夹归属插件
     * @param file 待获取文件路径
     * @return 对应文件
     */
    @JvmStatic
    fun getFile(plugin: String, file: String): File {
        return File(File(OishEternity.plugin.dataFolder.parent, File.separator + plugin), File.separator + file)
    }

    /**
     * 获取文件夹内文件(不存在时返回null)
     *
     * @param plugin 待获取文件夹归属插件
     * @param file 待获取文件路径
     * @return 对应文件
     */
    @JvmStatic
    fun getFileOrNull(plugin: String, file: String): File? {
        return File(File(OishEternity.plugin.dataFolder.parent, File.separator + plugin), File.separator + file).let {
            if (!it.exists()) null
            else it
        }
    }

    /**
     * 获取文件夹内文件(不存在时创建文件)
     *
     * @param plugin 待获取文件夹归属插件
     * @param file 待获取文件路径
     * @return 对应文件
     */
    @JvmStatic
    fun getFileOrCreate(plugin: String, file: String): File {
        return File(
            File(OishEternity.plugin.dataFolder.parent, File.separator + plugin),
            File.separator + file
        ).createFile()
    }

    /**
     * 深复制ConfigurationSection
     *
     * @return 对应ConfigurationSection的克隆
     */
    @JvmStatic
    fun ConfigurationSection.clone(): ConfigurationSection {
        val tempConfigSection = YamlConfiguration() as ConfigurationSection
        this.getKeys(false).forEach { key ->
            when (val value = this.get(key)) {
                is ConfigurationSection -> tempConfigSection.set(key, value.clone())
                is List<*> -> tempConfigSection.set(key, value.clone())
                else -> tempConfigSection.set(key, value)
            }
        }
        return tempConfigSection
    }

    /**
     * 深复制List
     *
     * @return 对应List的克隆
     */
    @JvmStatic
    fun List<*>.clone(): List<*> {
        return arrayListOf<Any?>().also { list ->
            forEach { value ->
                when (value) {
                    is ConfigurationSection -> list.add(value.clone())
                    is List<*> -> list.add(value.clone())
                    is Map<*, *> -> list.add(value.clone())
                    else -> list.add(value)
                }
            }
        }
    }

    /**
     * 深复制Map
     *
     * @return 对应Map的克隆
     */
    @JvmStatic
    fun Map<*, *>.clone(): Map<*, *> {
        return hashMapOf<Any?, Any?>().also { map ->
            forEach { (key, value) ->
                when (value) {
                    is ConfigurationSection -> map[key] = value.clone()
                    is List<*> -> map[key] = value.clone()
                    is Map<*, *> -> map[key] = value.clone()
                    else -> map[key] = value
                }
            }
        }
    }

    /**
     * 获取文件中所有ConfigurationSection
     *
     * @return 文件中所有ConfigurationSection
     */
    @JvmStatic
    fun File.getConfigSections(): ArrayList<ConfigurationSection> {
        val list = ArrayList<ConfigurationSection>()
        val config = YamlConfiguration.loadConfiguration(this)
        config.getKeys(false).forEach { key ->
            config.getConfigurationSection(key)?.let { list.add(it) }
        }
        return list
    }

    /**
     * 获取所有文件中所有ConfigurationSection
     *
     * @return 文件中所有ConfigurationSection
     */
    @JvmStatic
    fun ArrayList<File>.getConfigSections(): ArrayList<ConfigurationSection> {
        val list = ArrayList<ConfigurationSection>()
        for (file: File in this) {
            list.addAll(file.getConfigSections())
        }
        return list
    }


    /**
     * 获取文件中所有顶级节点内容
     *
     * @return 文件中所有顶级节点内容
     */
    @JvmStatic
    fun File.getContents(): ArrayList<Any> {
        val list = ArrayList<Any>()
        val config = YamlConfiguration.loadConfiguration(this)
        config.getKeys(false).forEach { key ->
            config.get(key)?.let { list.add(it) }
        }
        return list
    }

    /**
     * 获取文件中所有顶级节点内容
     *
     * @return 文件中所有顶级节点内容
     */
    @JvmStatic
    fun ArrayList<File>.getContents(): ArrayList<Any> {
        val list = ArrayList<Any>()
        for (file: File in this) {
            list.addAll(file.getContents())
        }
        return list
    }

    /**
     * 用于 ConfigurationSection 转 HashMap
     * ConfigurationSection 中可能包含 Map, List, ConfigurationSection 及任意值
     * 所有值的处理都放在这个方法里循环调用了,
     * 所以参数和返回值都是Any
     *
     * @param data 待转换内容
     * @return 转换结果
     */
    @JvmStatic
    fun toMap(data: Any?): Any? {
        when (data) {
            is ConfigurationSection -> {
                val map = HashMap<String, Any>()
                data.getKeys(false).forEach { key ->
                    toMap(data.get(key))?.let { value -> map[key] = value }
                }
                return map
            }

            is Map<*, *> -> {
                val map = HashMap<String, Any>()
                for ((key, value) in data) {
                    toMap(value)?.let { map[key as String] = it }
                }
                return map
            }

            is List<*> -> {
                val list = ArrayList<Any>()
                for (value in data) {
                    toMap(value)?.let { list.add(it) }
                }
                return list
            }

            else -> {
                return data
            }
        }
    }

    /**
     * ConfigurationSection 转 HashMap
     * @return 转换结果
     */
    @JvmStatic
    fun ConfigurationSection.toMap(): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        this.getKeys(false).forEach { key ->
            toMap(this.get(key))?.let { value -> map[key] = value }
        }
        return map
    }

    /**
     * ConfigurationSection 转 String
     * @param id 转换后呈现的节点ID, 一般可以为this.name(针对MemorySection)
     * @return 转换结果
     */
    @JvmStatic
    fun ConfigurationSection.saveToString(id: String): String {
        val tempConfigSection = YamlConfiguration()
        tempConfigSection.set(id, this)
        return tempConfigSection.saveToString()
    }

    /**
     * String 转 ConfigurationSection
     * @param id 转换前使用的节点ID
     * @return 转换结果
     */
    @JvmStatic
    fun String.loadFromString(id: String): ConfigurationSection? {
        val tempConfigSection = YamlConfiguration()
        tempConfigSection.loadFromString(this)
        return tempConfigSection.getConfigurationSection(id)
    }

    /**
     * File 转 YamlConfiguration
     * @return 转换结果
     */
    @JvmStatic
    fun File.loadConfiguration(): YamlConfiguration {
        return YamlConfiguration.loadConfiguration(this)
    }

    /**
     * ConfigurationSection 合并(后者覆盖前者, 在前者上操作)
     *
     * @param configSection 用于合并覆盖
     * @return 合并结果
     */
    @JvmStatic
    fun ConfigurationSection.coverWith(configSection: ConfigurationSection): ConfigurationSection {
        // 遍历所有键
        configSection.getKeys(false).forEach { key ->
            // 用于覆盖的值
            val coverValue = configSection.get(key)
            // 原有值
            val value = this.get(key)
            // 如果二者包含相同键
            if (value != null) {
                // 如果二者均为ConfigurationSection
                if (value is ConfigurationSection
                    && coverValue is ConfigurationSection
                ) {
                    // 合并
                    this.set(key, value.coverWith(coverValue))
                } else {
                    // 覆盖
                    this.set(key, coverValue)
                }
            } else {
                // 添加
                this.set(key, coverValue)
            }
        }
        return this
    }

    /**
     * 保存默认文件(不进行替换)
     *
     * @param resourcePath 文件路径
     */
    @JvmStatic
    fun JavaPlugin.saveResourceNotWarn(resourcePath: String) {
        this.saveResourceNotWarn(resourcePath, File(this.dataFolder, resourcePath))
    }

    /**
     * 保存默认文件(不进行替换)
     *
     * @param resourcePath 文件路径
     * @param outFile 输出路径
     */
    @JvmStatic
    fun JavaPlugin.saveResourceNotWarn(resourcePath: String, outFile: File) {
        this.getResource(resourcePath.replace('\\', '/'))?.use { inputStream ->
            outFile.parentFile.createDirectory()
            if (!outFile.exists()) {
                FileOutputStream(outFile).use { fileOutputStream ->
                    var len: Int
                    val buf = ByteArray(1024)
                    while (inputStream.read(buf).also { len = it } > 0) {
                        (fileOutputStream as OutputStream).write(buf, 0, len)
                    }
                }
            }
        }
    }
}
