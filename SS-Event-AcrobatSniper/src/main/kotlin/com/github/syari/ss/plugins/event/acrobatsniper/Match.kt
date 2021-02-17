package com.github.syari.ss.plugins.event.acrobatsniper

import com.github.syari.spigot.api.event.register.EventRegister
import com.github.syari.spigot.api.event.register.Events
import com.github.syari.spigot.api.scheduler.runTaskTimer
import com.github.syari.ss.plugins.core.message.Message.title
import com.github.syari.ss.plugins.core.sound.CreateSound.sound
import com.github.syari.ss.plugins.event.acrobatsniper.Main.Companion.plugin
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class Match(val player1: Player, val player2: Player) {
    fun start() {
        teleportSpawn()
        preventMove()
        startCountDown()
    }

    private fun teleportSpawn() {
        spawnLocation1?.let(player1::teleport)
        spawnLocation2?.let(player2::teleport)
    }

    private fun preventMove() {
        val effect = PotionEffect(PotionEffectType.SLOW, 5 * 20, 1, true)
        player1.addPotionEffect(effect)
        player2.addPotionEffect(effect)
    }

    private fun startCountDown() {
        var time = 5
        val sound = sound(Sound.ENTITY_PLAYER_LEVELUP, 1F, 2F)
        plugin.runTaskTimer(20) {
            if (time == 0) {
                cancel()
            } else {
                val message = buildString {
                    append(">".repeat(time))
                    append(" $time ")
                    append("<".repeat(time))
                }
                plugin.server.onlinePlayers.forEach {
                    it.title("&6&l$message", "", 0, 15, 5)
                    sound.play(it)
                }
                time --
            }
        }
    }

    companion object {
        var spawnLocation1: Location? = null
        var spawnLocation2: Location? = null
    }

    object EventListener : EventRegister {
        override fun Events.register() {
            cancelEventIf<EntityShootBowEvent> {
                it.entity.hasPotionEffect(PotionEffectType.SLOW)
            }
            cancelEventIf<PlayerMoveEvent> {
                it.player.hasPotionEffect(PotionEffectType.SLOW)
            }
        }
    }
}
