package com.github.syari.ss.plugins.globalplayers

import com.github.syari.spigot.api.event.EventRegister
import com.github.syari.spigot.api.event.Events
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object SilentJoinMessage : EventRegister {
    override fun Events.register() {
        event<PlayerJoinEvent> {
            it.joinMessage = null
        }
        event<PlayerQuitEvent> {
            it.quitMessage = null
        }
    }
}
