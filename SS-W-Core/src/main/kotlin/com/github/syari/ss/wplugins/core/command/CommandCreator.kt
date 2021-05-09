package com.github.syari.ss.wplugins.core.command

import com.github.syari.ss.wplugins.core.permission.Permission
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.api.plugin.TabExecutor

class CommandCreator(val label: String, val messagePrefix: String) {
    companion object {
        /**
         * コマンドを作成し、登録します
         * @param label コマンド名 /label
         * @param messagePrefix メッセージの接頭
         * @param action コマンドの設定
         * @see CommandExecuteAction
         * @see CommandArgument
         */
        fun Plugin.command(label: String, messagePrefix: String, action: CommandCreator.() -> Unit) {
            CommandCreator(label, messagePrefix).apply(action).register(this)
        }
    }

    private val tabContainer = CommandTabContainer()
    private var executeAction: CommandExecuteAction.() -> Unit = {}

    fun tab(action: CommandTabContainer.() -> Unit) {
        tabContainer.action()
    }

    fun execute(action: CommandExecuteAction.() -> Unit) {
        executeAction = action
    }

    internal fun register(plugin: Plugin) {
        registerCommand(
            plugin,
            object : Command(label, Permission.admin), TabExecutor {
                override fun execute(
                    sender: CommandSender,
                    args: Array<out String>
                ) {
                    executeAction(CommandExecuteAction(messagePrefix, sender, CommandExecuteArgument(args)))
                }

                override fun onTabComplete(
                    sender: CommandSender,
                    args: Array<out String>
                ): List<String> {
                    return tabContainer.get(sender, args)
                }
            }
        )
    }

    private fun registerCommand(plugin: Plugin, command: Command) {
        plugin.proxy.pluginManager.registerCommand(plugin, command)
    }
}
