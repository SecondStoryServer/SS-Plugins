package com.github.syari.ss.plugins.commandblocker

import com.github.syari.ss.plugins.commandblocker.AvailableCommand.availableCommands
import com.github.syari.ss.plugins.core.code.EventRegister
import com.github.syari.ss.plugins.core.code.Events
import com.github.syari.ss.plugins.core.message.Message.action
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerCommandSendEvent

object EventListener : EventRegister {
    override fun Events.register() {
        event<PlayerCommandSendEvent> {
            val player = it.player
            if (player.isOp) return@event
            it.commands.clear()
            it.commands.addAll(player.availableCommands)
        }
        event<PlayerCommandPreprocessEvent> {
            val player = it.player
            if (player.isOp) return@event
            val label = it.message.split(Regex("\\s+"), 2)[0].substring(1).toLowerCase()
            if (player.availableCommands.contains(label)) return@event
            it.isCancelled = true
            player.action("&c&l&n実行できないコマンドです")
        }
    }
}
