package com.github.syari.ss.plugins.assist

import com.github.syari.ss.plugins.core.code.EventRegister
import com.github.syari.ss.plugins.core.code.ListenerFunctions
import com.github.syari.ss.plugins.core.event.CustomCancellableEvent
import com.github.syari.ss.plugins.core.scheduler.CreateScheduler.runLater
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerRespawnEvent

object SpawnOverride : EventRegister {
    override fun ListenerFunctions.events() {
        event<PlayerRespawnEvent> {
            it.respawnLocation = location ?: return@event
        }
        event<PlayerJoinEvent> {
            val overrideLocation = location ?: return@event
            val player = it.player
            if (JoinLocationOverrideEvent(player).callEvent()) {
                plugin.runLater(1) {
                    player.teleport(overrideLocation)
                }
            }
        }
    }

    var location: Location? = null

    class JoinLocationOverrideEvent(val player: Player) : CustomCancellableEvent()
}
