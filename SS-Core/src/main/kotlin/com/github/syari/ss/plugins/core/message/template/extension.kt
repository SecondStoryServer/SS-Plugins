package com.github.syari.ss.plugins.core.message.template

import com.github.syari.spigot.api.command.execute.CommandExecuteAction
import com.github.syari.spigot.api.util.string.toColor
import com.github.syari.ss.plugins.core.message.Message.send
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.command.CommandSender

fun CommandExecuteAction.templateMessage(prefix: String) = TemplateMessage(sender, prefix)

fun CommandSender.sendTemplate(prefix: String, message: TextComponent) {
    send(TextComponent("&b[$prefix] ".toColor()).apply { addExtra(message) })
}

fun CommandSender.sendTemplate(prefix: String, message: String) {
    send("&b[$prefix] &r$message")
}

fun CommandSender.sendTemplateError(prefix: String, message: String) {
    sendTemplate(prefix, "&c$message")
}

fun CommandSender.sendTemplateList(prefix: String, description: String?, list: Iterable<String>) {
    if (description.isNullOrEmpty().not()) sendTemplate(prefix, "&f$description")
    sendMessage(list.joinToString("\n") { "&7- &a$it" })
}

fun CommandSender.sendTemplateCommandHelp(prefix: String, vararg command: Pair<String, String>) {
    sendTemplateList(prefix, "コマンド一覧", command.map { "/${it.first} &7${it.second}" })
}
