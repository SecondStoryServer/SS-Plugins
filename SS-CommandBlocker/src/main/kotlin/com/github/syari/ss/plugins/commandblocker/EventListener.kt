package com.github.syari.ss.plugins.commandblocker

import com.github.syari.ss.plugins.commandblocker.AvailableCommand.isAvailableCommand
import com.github.syari.ss.plugins.core.message.Message.action
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

object EventListener: Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(e: PlayerCommandPreprocessEvent) {
        val p = e.player
        if (p.isOp) return
        val label = e.message.split(Regex("\\s+"), 2)[0].substring(1).toLowerCase()
        if (p.isAvailableCommand(label)) return
        e.isCancelled = true
        p.action("&c&l&n実行できないコマンドです")
    }
}