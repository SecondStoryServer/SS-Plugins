@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.command

import com.github.syari.ss.plugins.core.Main.Companion.plugin
import com.github.syari.ss.plugins.core.console
import org.bukkit.command.CommandSender

object RunCommand {
    /**
     * コマンドを実行します
     * @param sender 実行元
     * @param command 実行するコマンド
     */
    fun runCommand(
        sender: CommandSender,
        command: String
    ) {
        plugin.server.dispatchCommand(sender, command)
    }

    /**
     * コンソールからコマンドを実行します
     * @param command 実行するコマンド
     */
    fun runCommandFromConsole(command: String) {
        runCommand(console, command)
    }
}
