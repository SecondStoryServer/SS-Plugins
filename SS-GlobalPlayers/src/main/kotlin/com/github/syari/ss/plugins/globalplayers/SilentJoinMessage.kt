package com.github.syari.ss.plugins.globalplayers

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object SilentJoinMessage : Listener {
    @EventHandler
    fun on(e: PlayerJoinEvent) {
        e.joinMessage = null
    }

    @EventHandler
    fun on(e: PlayerQuitEvent) {
        e.quitMessage = null
    }
}
