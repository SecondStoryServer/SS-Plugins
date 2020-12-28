package com.github.syari.ss.plugins.core.command.create

import com.github.syari.ss.plugins.core.Main.Companion.plugin
import org.bukkit.command.Command
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

object CreateCommand {
    /**
     * 一般的なタブ補完
     * @param arg 引数が一致した場合にタブ補完されます
     * @param tab タブ補完の内容
     * @return [CommandTab.Base]
     */
    fun tab(
        vararg arg: String, tab: (Pair<CommandSender, CommandArgument>) -> CommandTabElement?
    ): CommandTab.Base {
        return CommandTab.Base(arg.toList(), tab)
    }

    /**
     * 設定のタブ補完
     * @param arg 引数が一致した場合にタブ補完されます
     * @param flag 設定に対応するタブ補完の内容
     * @return [CommandTab.Flag]
     */
    fun flag(
        arg: String, vararg flag: Pair<String, CommandTabElement>
    ): CommandTab.Flag {
        return CommandTab.Flag(arg, flag.toMap())
    }

    /**
     * タブ補完の要素
     * @param element 要素
     * @return [CommandTabElement]
     */
    fun element(element: Iterable<String>?): CommandTabElement {
        return CommandTabElement(element ?: listOf())
    }

    /**
     * タブ補完の要素
     * @param element 要素
     * @return [CommandTabElement]
     */
    fun element(vararg element: String): CommandTabElement {
        return element(element.toList())
    }

    /**
     * タブ補完の要素
     * @param condition 条件
     * @param element 条件に一致した場合の要素
     * @param unlessElement 条件に一致しなかった場合の要素
     * @return [CommandTabElement]
     */
    fun elementIf(
        condition: Boolean, element: Iterable<String>?, unlessElement: Iterable<String>? = listOf()
    ): CommandTabElement {
        return element(if (condition) element else unlessElement)
    }

    /**
     * タブ補完の要素
     * @param condition 条件
     * @param element 条件に一致した場合の要素
     * @param unlessElement 条件に一致しなかった場合の要素
     * @return [CommandTabElement]
     */
    fun elementIf(
        condition: Boolean, vararg element: String, unlessElement: Iterable<String>? = listOf()
    ): CommandTabElement {
        return elementIf(condition, element.toList(), unlessElement)
    }

    /**
     * タブ補完の要素
     * @param sender CommandSender
     * @param element sender.isOpが真であった場合の要素
     * @param unlessElement sender.isOpが偽であった場合の要素
     * @return [CommandTabElement]
     */
    fun elementIfOp(
        sender: CommandSender, element: Iterable<String>?, unlessElement: Iterable<String>? = listOf()
    ): CommandTabElement {
        return elementIf(sender.isOp, element, unlessElement)
    }

    /**
     * タブ補完の要素
     * @param sender CommandSender
     * @param element sender.isOpが真であった場合の要素
     * @param unlessElement sender.isOpが偽であった場合の要素
     * @return [CommandTabElement]
     */
    fun elementIfOp(
        sender: CommandSender, vararg element: String, unlessElement: Iterable<String>? = listOf()
    ): CommandTabElement {
        return elementIfOp(sender, element.toList(), unlessElement)
    }

    /**
     * コマンドを作成し、登録します
     * @param plugin 登録するプラグイン
     * @param label コマンド名 /label
     * @param messagePrefix メッセージの接頭
     * @param tabs タブ補完
     * @param execute コマンドの処理
     * @see CommandMessage
     * @see CommandArgument
     */
    fun command(
        plugin: JavaPlugin, label: String, messagePrefix: String, vararg tabs: CommandTab, execute: CommandMessage.(CommandSender, CommandArgument) -> Unit
    ) {
        registerCommand(plugin, object: Command(label) {
            override fun execute(
                sender: CommandSender, commandLabel: String, args: Array<out String>
            ): Boolean {
                val message = CommandMessage(messagePrefix, sender)
                execute.invoke(
                    message, sender, CommandArgument(args, message)
                )
                return true
            }

            override fun tabComplete(
                sender: CommandSender, alias: String, args: Array<out String>
            ): List<String> {
                return tabs.flatMap { tab ->
                    when (tab) {
                        is CommandTab.Base -> {
                            val element = tab.complete(
                                sender to CommandArgument(args, CommandMessage(messagePrefix, sender))
                            )?.element ?: return@flatMap listOf()
                            when {
                                tab.arg.isNotEmpty() -> tab.arg.flatMap { arg ->
                                    val splitArg = arg.split("\\s+".toRegex())
                                    if (splitArg.size <= args.size && splitArg.last() == "**") {
                                        element
                                    } else if (splitArg.size == args.lastIndex) {
                                        val completed = if (arg.contains('*')) {
                                            buildString {
                                                splitArg.forEachIndexed { index, word ->
                                                    append(if (word == "*") args[index] else word)
                                                }
                                            }.substringBeforeLast(" ")
                                        } else {
                                            arg
                                        }
                                        val joinArg = args.joinToString(" ").toLowerCase()
                                        element.filter {
                                            "$completed $it".toLowerCase().startsWith(joinArg)
                                        }
                                    } else {
                                        listOf()
                                    }
                                }
                                args.size == 1 -> element
                                else -> listOf()
                            }
                        }
                        is CommandTab.Flag -> {
                            val elementList = mutableSetOf<String>()
                            val splitArg = tab.arg.split("\\s+".toRegex())
                            splitArg.forEachIndexed { index, split ->
                                if (split != "*" && split.equals(args.getOrNull(index), true)) {
                                    return@flatMap listOf()
                                }
                            }
                            val enterText = args.getOrNull(args.size - 1) ?: return@flatMap listOf()
                            if ((args.lastIndex - splitArg.size) % 2 == 0) {
                                val element = tab.flag.keys.toMutableSet()
                                for (index in splitArg.size until args.lastIndex step 2) {
                                    element.remove(args[index].toLowerCase())
                                }
                                elementList.addAll(element)
                            } else {
                                tab.flag[args.getOrNull(args.size - 2)?.toLowerCase()]?.let {
                                    elementList.addAll(it)
                                }
                            }
                            elementList.filter {
                                it.toLowerCase().startsWith(enterText)
                            }
                        }
                    }
                }.sorted()
            }
        })
    }

    private fun registerCommand(
        plugin: JavaPlugin, command: Command
    ) {
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

    /**
     * 全オフラインプレイヤーの名前一覧を取得します
     */
    val offlinePlayers get() = CommandTabElement(plugin.server.offlinePlayers.mapNotNull { it?.name })

    /**
     * 全オンラインプレイヤーの名前一覧を取得します
     */
    val onlinePlayers get() = CommandTabElement(plugin.server.onlinePlayers.mapNotNull { it?.name })
}