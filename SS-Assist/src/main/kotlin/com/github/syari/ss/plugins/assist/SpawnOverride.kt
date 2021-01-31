package com.github.syari.ss.plugins.assist

import com.github.syari.ss.plugins.core.code.EventRegister
import com.github.syari.ss.plugins.core.code.ListenerFunctions
import com.github.syari.ss.plugins.core.scheduler.CreateScheduler.runLater
import org.bukkit.Location
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerRespawnEvent

object SpawnOverride : EventRegister {
    override fun ListenerFunctions.events() {
        event<PlayerRespawnEvent> {
            it.respawnLocation = location ?: return@event
        }
        event<PlayerJoinEvent> { e ->
            plugin.runLater(1) {
                location?.let { e.player.teleport(it) }
            }
        }
    }

    var location: Location? = null
}
