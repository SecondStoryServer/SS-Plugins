package com.github.syari.ss.wplugins.core.config

import com.github.syari.ss.wplugins.core.config.CustomConfig.Companion.YamlConfigurationProvider
import com.github.syari.ss.wplugins.core.message.Message.send
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.config.Configuration
import java.io.Reader

/**
 * @param plugin コンフィグがあるプラグイン
 * @param output メッセージの出力先
 * @param uniqueName 識別名
 * @param reader コンフィグの内容
 */
class CustomReaderConfig internal constructor(
    override val plugin: Plugin,
    private val output: CommandSender,
    private val uniqueName: String,
    reader: Reader
) : CustomConfig {
    override val config: Configuration = YamlConfigurationProvider.load(reader)

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
