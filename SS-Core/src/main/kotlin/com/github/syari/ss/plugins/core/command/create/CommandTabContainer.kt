@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.command.create

import org.bukkit.command.CommandSender

class CommandTabContainer {
    private val list = mutableListOf<CommandTab>()

    fun arg(vararg arg: String, complete: CommandCompleteElement.() -> CommandTabElement?) {
        list.add(CommandTab.Argument(arg.toList(), complete))
    }

    fun flag(arg: String, vararg flag: Pair<String, CommandTabElement>) {
        list.add(CommandTab.Flag(arg, flag.toMap()))
    }

    internal fun get(sender: CommandSender, args: Array<out String>): List<String> {
        return list.flatMap { tab ->
            when (tab) {
                is CommandTab.Argument -> {
                    val completeElement = CommandCompleteElement(sender, CommandArgument(args))
                    val element = tab.complete(completeElement)?.element ?: return@flatMap listOf()
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
}
