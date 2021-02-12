@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.message.template

import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.command.CommandSender

class TemplateMessage(private val sender: CommandSender, private val prefix: String) {
    fun send(message: String) {
        sender.sendTemplate(prefix, message)
    }

    fun send(message: TextComponent) {
        sender.sendTemplate(prefix, message)
    }

    fun sendError(message: String) {
        sender.sendTemplateError(prefix, message)
    }

    fun sendList(description: String?, list: Iterable<String>) {
        sender.sendTemplateList(prefix, description, list)
    }

    fun sendCommandHelp(vararg command: Pair<String, String>) {
        sender.sendTemplateCommandHelp(prefix, *command)
    }
}
