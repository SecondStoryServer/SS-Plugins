package com.github.syari.ss.plugins.event.acrobatsniper

import com.github.syari.spigot.api.event.register.EventRegister
import com.github.syari.spigot.api.event.register.Events
import com.github.syari.spigot.api.scheduler.runTaskLater
import com.github.syari.spigot.api.util.uuid.UUIDPlayer
import org.bukkit.Effect
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerToggleFlightEvent

object DoubleJump : EventRegister {
    private val allowFlightPlayers = mutableSetOf<UUIDPlayer>()

    private inline val Player.isAdventure
        get() = gameMode == GameMode.ADVENTURE

    override fun Events.register() {
        event<PlayerJoinEvent> {
            val player = it.player
            val uuidPlayer = UUIDPlayer.from(player)
            if (allowFlightPlayers.contains(uuidPlayer)) {
                allowFlightPlayers.remove(uuidPlayer)
                player.allowFlight = false
                player.isFlying = false
            }
        }
        event<PlayerToggleFlightEvent> {
            val player = it.player
            val uuidPlayer = UUIDPlayer.from(player)
            if (player.isAdventure.not()) {
                if (player.gameMode != GameMode.CREATIVE) {
                    player.isFlying = false
                    player.allowFlight = false
                }
                return@event
            }
            it.isCancelled = true
            player.allowFlight = false
            player.isFlying = false
            player.velocity = player.location.direction.multiply(1.0).setY(1)
            allowFlightPlayers.add(uuidPlayer)
            val location = player.location
            location.world.playEffect(location, Effect.SMOKE, 5)
            location.world.playSound(location, Sound.ENTITY_ENDER_DRAGON_SHOOT, 2.0f, 0.0f)
            plugin.runTaskLater(20) {
                if (player.isFlying) {
                    player.allowFlight = false
                    allowFlightPlayers.remove(uuidPlayer)
                }
            }
        }
        event<PlayerMoveEvent> {
            val player = it.player
            val location = player.location
            val underBlock = location.subtract(0.0, 1.0, 0.0).block
            if (player.isAdventure && underBlock.type != Material.AIR) {
                player.allowFlight = true
            }
        }
    }
}
