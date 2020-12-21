package com.github.syari.ss.plugins.votifier

import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.command.create.CreateCommand.command
import com.github.syari.ss.plugins.core.command.create.CreateCommand.elementIf
import com.github.syari.ss.plugins.core.command.create.CreateCommand.tab
import com.github.syari.ss.plugins.votifier.BootstrapBuilder.reload
import com.github.syari.ss.plugins.votifier.Main.Companion.plugin
import org.bukkit.command.ConsoleCommandSender

object CommandCreator: OnEnable {
    override fun onEnable() {
        command(plugin, "votifier", "SS-Votifier", tab { (sender, _) ->
            elementIf(sender is ConsoleCommandSender, "reload")
        }) { sender, args ->
            when (args.whenIndex(0)) {
                "reload" -> {
                    if (sender !is ConsoleCommandSender) return@command sendError("コンソールからのみ実行可能です")
                    sendWithPrefix("コンフィグをリロードします")
                    reload(sender)
                }
                else -> {
                    sendHelp(
                        "votifier reload" to "コンフィグをリロードします"
                    )
                }
            }
        }
    }
}