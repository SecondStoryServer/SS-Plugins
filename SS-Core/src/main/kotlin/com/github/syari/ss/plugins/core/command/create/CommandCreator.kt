package com.github.syari.ss.plugins.core.command.create

import org.bukkit.command.Command
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

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
        fun JavaPlugin.command(label: String, messagePrefix: String, action: CommandCreator.() -> Unit) {
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

    internal fun register(plugin: JavaPlugin) {
        registerCommand(
            plugin,
            object : Command(label) {
                override fun execute(
                    sender: CommandSender,
                    commandLabel: String,
                    args: Array<out String>
                ): Boolean {
                    executeAction(CommandExecuteAction(messagePrefix, sender, CommandExecuteArgument(args)))
                    return true
                }

                override fun tabComplete(
                    sender: CommandSender,
                    alias: String,
                    args: Array<out String>
                ): List<String> {
                    return tabContainer.get(sender, args)
                }
            }
        )
    }

    private fun registerCommand(plugin: JavaPlugin, command: Command) {
        try {
            val commandMapField = plugin.server.javaClass.getDeclaredField("commandMap")
            commandMapField.isAccessible = true
            val commandMap = commandMapField.get(plugin.server) as CommandMap
            commandMap.register(plugin.name, command)
            commandMapField.isAccessible = false
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}
