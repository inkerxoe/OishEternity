package me.inkerxoe.oishplugin.eternity.utils

import taboolib.common.platform.function.getDataFolder
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.Reader
import java.security.MessageDigest

/**
 * OishEternity
 * me.inkerxoe.oishplugin.eternity.utils
 *
 * @author InkerXoe
 * @since 2024/2/4 09:27
 */
/**
 * 文件相关工具类
 */
object FileUtil {
    /**
     * 解析文件编码
     *
     * @param file 待解析文件
     * @return 编码类型
     */
    @JvmStatic
    fun charset(file: File): String {
        var charset = "GBK"
        val first3Bytes = ByteArray(3)
        try {
            var checked = false
            val bis = BufferedInputStream(FileInputStream(file))
            bis.mark(0)
            var read = bis.read(first3Bytes, 0, 3)
            if (read == -1) {
                bis.close()
                return charset
            } else if (first3Bytes[0] == 0xFF.toByte() && first3Bytes[1] == 0xFE.toByte()) {
                charset = "UTF-16LE"
                checked = true
            } else if (first3Bytes[0] == 0xFE.toByte() && first3Bytes[1] == 0xFF.toByte()) {
                charset = "UTF-16BE"
                checked = true
            } else if (first3Bytes[0] == 0xEF.toByte() && first3Bytes[1] == 0xBB.toByte() && first3Bytes[2] == 0xBF.toByte()) {
                charset = "UTF-8"
                checked = true
            }
            bis.reset()
            if (!checked) {
                while (bis.read().also { read = it } != -1) {
                    if (read >= 0xF0) break
                    if (read in 0x80..0xBF)
                        break
                    if (read in 0xC0..0xDF) {
                        read = bis.read()
                        if (read in 0x80..0xBF)
                            continue else break
                    } else if (read in 0xE0..0xEF) {
                        read = bis.read()
                        if (read in 0x80..0xBF) {
                            read = bis.read()
                            if (read in 0x80..0xBF) {
                                charset = "UTF-8"
                                break
                            } else break
                        } else break
                    }
                }
            }
            bis.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return charset
    }

    /**
     * 读取文本文件
     *
     * @param file 文本文件
     * @return 文件文本
     */
    @JvmStatic
    fun readText(file: File): String {
        return file.readText()
    }

    /**
     * 读取Reader文本
     *
     * @param reader 待读取Reader
     * @return 文件文本
     */
    @JvmStatic
    fun readText(reader: Reader): String {
        return reader.readText()
    }

    /**
     * 写入文本文件
     *
     * @param file 文本文件
     * @param text 文件文本
     */
    @JvmStatic
    fun writeText(file: File, text: String) {
        file.writeText(text)
    }

    /**
     * 获取文件sha1码
     *
     * @return sha1码
     */
    @JvmStatic
    fun File.sha1(): String {
        FileInputStream(this).use { fis ->
            val digest = MessageDigest.getInstance("SHA1")
            val buffer = ByteArray(8192)
            var len: Int
            while (fis.read(buffer).also { len = it } != -1) {
                digest.update(buffer, 0, len)
            }
            return digest.digest().joinToString("") { "%02x".format(it) }
        }
    }

    @JvmStatic
    fun File.createFile(): File {
        if (!exists()) {
            if (!parentFile.exists()) {
                parentFile.mkdirs()
            }
            createNewFile()
        }
        return this
    }

    @JvmStatic
    fun File.createDirectory(): File {
        if (!exists()) {
            mkdirs()
        }
        return this
    }
}