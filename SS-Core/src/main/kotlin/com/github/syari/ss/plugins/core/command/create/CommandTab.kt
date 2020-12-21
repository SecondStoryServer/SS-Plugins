package com.github.syari.ss.plugins.core.command.create

import org.bukkit.command.CommandSender

sealed class CommandTab {
    /**
     * @see CreateCommand.tab
     */
    class Base internal constructor(
        val arg: List<String>, val complete: (Pair<CommandSender, CommandArgument>) -> CommandTabElement?
    ): CommandTab()

    /**
     * @see CreateCommand.flag
     */
    class Flag internal constructor(
        val arg: String, val flag: Map<String, CommandTabElement>
    ): CommandTab()
}