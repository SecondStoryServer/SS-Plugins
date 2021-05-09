package com.github.syari.ss.plugins.commandblocker

import com.github.syari.spigot.api.event.events
import com.github.syari.ss.plugins.commandblocker.AvailableCommand.availableCommands
import com.github.syari.ss.plugins.commandblocker.Main.Companion.plugin
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.message.Message.action
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerCommandSendEvent

object EventListener : OnEnable {
    override fun onEnable() {
        plugin.events {
            event<PlayerCommandSendEvent> {
                val player = it.player
                if (player.isOp) return@event
                it.commands.clear()
                it.commands.addAll(player.availableCommands)
            }
            event<PlayerCommandPreprocessEvent> {
                val player = it.player
                if (player.isOp) return@event
                val label = it.message.split(Regex("\\s+"), 2)[0].substring(1).lowercase()
                if (player.availableCommands.contains(label)) return@event
                it.isCancelled = true
                player.action("&c&l&n実行できないコマンドです")
            }
        }
    }
}
