package com.github.syari.ss.plugins.commandblocker

import com.github.syari.ss.plugins.commandblocker.AvailableCommand.availableCommands
import com.github.syari.ss.plugins.core.message.Message.action
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerCommandSendEvent

object EventListener: Listener {
    @EventHandler
    fun on(e: PlayerCommandSendEvent) {
        e.commands.clear()
        e.commands.addAll(e.player.availableCommands)
    }

    @EventHandler(ignoreCancelled = true)
    fun on(e: PlayerCommandPreprocessEvent) {
        val p = e.player
        if (p.isOp) return
        val label = e.message.split(Regex("\\s+"), 2)[0].substring(1).toLowerCase()
        if (p.availableCommands.contains(label)) return
        e.isCancelled = true
        p.action("&c&l&n実行できないコマンドです")
    }
}