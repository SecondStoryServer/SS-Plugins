package com.github.syari.ss.plugins.playerdatastore

import com.github.syari.spigot.api.event.EventRegister
import com.github.syari.spigot.api.event.Events
import com.github.syari.ss.plugins.playerdatastore.PlayerData.Companion.storeData
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.spigotmc.event.player.PlayerSpawnLocationEvent

object EventListener : EventRegister {
    override fun Events.register() {
        event<PlayerJoinEvent> {
            it.player.storeData.inventory.load()
        }
        event<PlayerQuitEvent> {
            it.player.storeData.unloadAll()
        }
        event<PlayerSpawnLocationEvent> {
            it.spawnLocation = it.player.storeData.location.get() ?: return@event
        }
    }
}
