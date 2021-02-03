package com.github.syari.ss.plugins.assist

import com.github.syari.ss.plugins.core.code.EventRegister
import com.github.syari.ss.plugins.core.code.ListenerFunctions
import org.bukkit.Location
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerRespawnEvent
import org.spigotmc.event.player.PlayerSpawnLocationEvent

object SpawnOverride : EventRegister {
    override fun ListenerFunctions.events() {
        event<PlayerRespawnEvent> {
            it.respawnLocation = location ?: return@event
        }
        event<PlayerSpawnLocationEvent>(EventPriority.LOW) {
            it.spawnLocation = location ?: return@event
        }
    }

    var location: Location? = null
}
