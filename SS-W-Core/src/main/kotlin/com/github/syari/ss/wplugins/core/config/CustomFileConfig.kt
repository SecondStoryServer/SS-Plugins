package com.github.syari.ss.wplugins.core.config

import com.github.syari.ss.wplugins.core.config.CustomConfig.Companion.YamlConfigurationProvider
import com.github.syari.ss.wplugins.core.message.Message.send
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.config.Configuration
import java.io.File
import java.io.IOException

/**
 * @param plugin コンフィグがあるプラグイン
 * @param output メッセージの出力先
 * @param fileName ファイル名
 * @param directory ファイルの親フォルダ
 * @param deleteIfEmpty 中身が存在しなければ消去する
 */
class CustomFileConfig internal constructor(
    override val plugin: Plugin,
    private val output: CommandSender,
    val fileName: String,
    private val directory: File,
    private val deleteIfEmpty: Boolean,
    default: Map<String, Any> = emptyMap()
) : CustomConfig {
    private var file = File(directory, fileName)
    override val config: Configuration
    private val filePath: String = file.path.substringAfter(plugin.dataFolder.path).substring(1)

    init {

        val writeDefault = if (!file.exists()) {
            try {
                file.createNewFile()
                plugin.logger.info("$filePath の作成に成功しました")
                true
            } catch (ex: IOException) {
                plugin.logger.severe("$filePath の作成に失敗しました")
                false
            }
        } else if (file.length() == 0L && deleteIfEmpty) {
            plugin.logger.warning("$filePath は中身が存在しないので削除されます")
            delete()
            false
        } else {
            false
        }
        config = YamlConfigurationProvider.load(file)
        if (writeDefault && default.isNotEmpty()) {
            default.forEach { (key, value) ->
                set(key, value)
            }
            save()
        }
    }

    /**
     * 拡張子を除いたファイル名
     */
    val fileNameWithoutExtension = file.nameWithoutExtension

    /**
     * @param path コンフィグパス
     * @param value 上書きする値
     * @param save 上書き後に保存する default: false
     */
    fun set(
        path: String,
        value: Any?,
        save: Boolean = false
    ) {
        config.set(path, value)
        if (save) save()
    }

    /**
     * ファイルの名前を変更します
     * @param newName 新しい名前
     */
    fun rename(newName: String): Boolean {
        if (file.list()?.contains(newName) != false) return false
        return try {
            file.renameTo(File(directory, newName))
            true
        } catch (ex: SecurityException) {
            false
        } catch (ex: NullPointerException) {
            false
        }
    }

    /**
     * ファイルの変更を保存します
     */
    fun save() {
        YamlConfigurationProvider.save(config, file)
        if (deleteIfEmpty && file.length() == 0L) {
            delete()
        }
    }

    /**
     * ファイルを削除します
     */
    fun delete() {
        file.delete()
        plugin.logger.info("$filePath の削除に成功しました")
    }

    /**
     * エラーを出力します
     * ```
     * Format: "&6[$filePath|$path] &c$message"
     * ```
     * @param path コンフィグパス
     * @param message 本文
     */
    override fun sendError(
        path: String,
        message: String
    ) {
        output.send("&6[$filePath|$path] &c$message")
    }
}
