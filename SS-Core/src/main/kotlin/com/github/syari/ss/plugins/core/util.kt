package com.github.syari.ss.plugins.core

import com.github.syari.ss.plugins.core.Main.Companion.plugin
import org.bukkit.command.ConsoleCommandSender

val console: ConsoleCommandSender
    get() = plugin.server.consoleSender
