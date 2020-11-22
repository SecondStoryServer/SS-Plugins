package com.github.syari.ss.plugins.core.message

import com.github.syari.ss.plugins.core.message.Message.send
import org.bukkit.plugin.java.JavaPlugin

class ConsoleLogger(private val plugin: JavaPlugin) {
    /**
     * コンソールへメッセージを送信します
     * @param message 本文
     */
    fun send(message: String) {
        plugin.server.consoleSender.send(message)
    }

    /**
     * [java.util.logging.Logger.info] を実行します
     * @param message 本文
     */
    fun info(message: String) {
        plugin.server.logger.info(message)
    }

    /**
     * [java.util.logging.Logger.warning] を実行します
     * @param message 本文
     */
    fun warn(message: String) {
        plugin.server.logger.warning(message)
    }

    /**
     * [java.util.logging.Logger.severe] を実行します
     * @param message 本文
     */
    fun error(message: String) {
        plugin.server.logger.severe(message)
    }
}