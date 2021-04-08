package com.github.syari.ss.plugins.assist

import com.github.syari.spigot.api.event.events
import com.github.syari.ss.plugins.assist.Main.Companion.plugin
import com.github.syari.ss.plugins.core.code.OnEnable
import org.bukkit.Location
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerRespawnEvent
import org.spigotmc.event.player.PlayerSpawnLocationEvent

object SpawnOverride : OnEnable {
    override fun onEnable() {
        plugin.events {
            event<PlayerRespawnEvent> {
                it.respawnLocation = location ?: return@event
            }
            event<PlayerSpawnLocationEvent>(EventPriority.LOW) {
                it.spawnLocation = location ?: return@event
            }
        }
    }

    var location: Location? = null
}
