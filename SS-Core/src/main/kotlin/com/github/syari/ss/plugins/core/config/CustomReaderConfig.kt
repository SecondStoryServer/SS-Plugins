package com.github.syari.ss.plugins.core.config

import com.github.syari.ss.plugins.core.message.Message.send
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.Reader

/**
 * @param plugin コンフィグがあるプラグイン
 * @param output メッセージの出力先
 * @param uniqueName 識別名
 * @param reader コンフィグの内容
 */
class CustomReaderConfig internal constructor(
    override val plugin: JavaPlugin,
    private val output: CommandSender,
    private val uniqueName: String,
    reader: Reader
): CustomConfig {
    override val config: YamlConfiguration = YamlConfiguration.loadConfiguration(reader)

    /**
     * エラーを出力します
     * ```
     * Format: "&6[uniqueName|$path] &c$message"
     * ```
     * @param path コンフィグパス
     * @param message 本文
     */
    override fun sendError(
        path: String,
        message: String
    ) {
        output.send("&6[$uniqueName|$path] &c$message")
    }
}