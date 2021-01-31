package com.github.syari.ss.plugins.commandblocker

import com.github.syari.ss.plugins.commandblocker.AvailableCommand.availableCommands
import com.github.syari.ss.plugins.core.code.EventRegister
import com.github.syari.ss.plugins.core.code.ListenerFunctions
import com.github.syari.ss.plugins.core.message.Message.action
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerCommandSendEvent

object EventListener : EventRegister {
    override fun ListenerFunctions.events() {
        event<PlayerCommandSendEvent> { e ->
            val p = e.player
            if (p.isOp) return@event
            e.commands.clear()
            e.commands.addAll(p.availableCommands)
        }
        event<PlayerCommandPreprocessEvent> { e ->
            val p = e.player
            if (p.isOp) return@event
            val label = e.message.split(Regex("\\s+"), 2)[0].substring(1).toLowerCase()
            if (p.availableCommands.contains(label)) return@event
            e.isCancelled = true
            p.action("&c&l&n実行できないコマンドです")
        }
    }
}
