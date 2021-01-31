package com.github.syari.ss.plugins.globalplayers

import com.github.syari.ss.plugins.core.code.EventRegister
import com.github.syari.ss.plugins.core.code.ListenerFunctions
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object SilentJoinMessage : EventRegister {
    override fun ListenerFunctions.events() {
        event<PlayerJoinEvent> {
            it.joinMessage = null
        }
        event<PlayerQuitEvent> {
            it.quitMessage = null
        }
    }
}
