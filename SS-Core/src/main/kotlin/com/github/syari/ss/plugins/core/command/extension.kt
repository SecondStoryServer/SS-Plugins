package com.github.syari.ss.plugins.core.command

import com.github.syari.spigot.api.command.CommandArgument
import com.github.syari.ss.plugins.core.message.template.ConstantMessage.NotEnterPlayerName
import com.github.syari.ss.plugins.core.message.template.ConstantMessage.NotFoundPlayer
import com.github.syari.ss.plugins.core.message.template.TemplateMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player

fun CommandArgument.getPlayer(index: Int, template: TemplateMessage): Player? {
    val playerName = getOrNull(index) ?: return template.sendError(NotEnterPlayerName).run { null }
    return Bukkit.getPlayer(playerName) ?: return template.sendError(NotFoundPlayer).run { null }
}
