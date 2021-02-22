package com.github.syari.ss.plugins.event.acrobatsniper

import com.github.syari.spigot.api.event.register.EventRegister
import com.github.syari.spigot.api.event.register.Events
import com.github.syari.spigot.api.scheduler.runTaskLater
import com.github.syari.spigot.api.scheduler.runTaskTimer
import com.github.syari.ss.plugins.core.message.Message.broadcast
import com.github.syari.ss.plugins.core.message.Message.title
import com.github.syari.ss.plugins.core.sound.CreateSound.sound
import com.github.syari.ss.plugins.event.acrobatsniper.Main.Companion.plugin
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class Match(private val player1: MatchPlayer, private val player2: MatchPlayer) {
    init {
        player1.match = this
        player2.match = this
    }

    fun start() {
        addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 5 * 20)
        addPotionEffect(PotionEffectType.WEAKNESS, 5 * 20)
        addPotionEffect(PotionEffectType.SLOW, 5 * 20)
        teleportSpawn()
        startCountDown()
    }

    private fun teleportSpawn() {
        spawnLocation1?.let(player1.player::teleport)
        spawnLocation2?.let(player2.player::teleport)
    }

    private fun addPotionEffect(effectType: PotionEffectType, duration: Int) {
        val effect = PotionEffect(effectType, duration, 1, true, false, false)
        player1.player.addPotionEffect(effect)
        player2.player.addPotionEffect(effect)
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
                listOf(player1.player, player2.player).forEach {
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

        fun start(player1: Player, player2: Player) {
            val matchPlayer1 = MatchPlayer(player1, player2)
            val matchPlayer2 = MatchPlayer(player2, player1)
            Match(matchPlayer1, matchPlayer2).start()
        }
    }

    object EventListener : EventRegister {
        override fun Events.register() {
            event<EntityDeathEvent> {
                val player = it.entity as? Player ?: return@event
                val matchPlayer = MatchPlayer.get(player) ?: return@event
                it.isCancelled = true
                val enemyPlayer = matchPlayer.enemy
                val enemyName = enemyPlayer.displayName
                val playerName = player.displayName
                if (matchPlayer.life == 1) {
                    broadcast("&b[AcrobatSniper] &6$enemyName &fが &6$playerName &fに勝ちました")
                    MatchPlayer.remove(player)
                    val spawnLocation = player.world.spawnLocation
                    matchPlayer.match.addPotionEffect(PotionEffectType.WEAKNESS, 10 * 20)
                    var index = 0
                    plugin.runTaskTimer(10) {
                        if (14 < index) {
                            cancel()
                            player.teleport(spawnLocation)
                            enemyPlayer.teleport(spawnLocation)
                        } else {
                            val location = enemyPlayer.location.add(0.0, 2.0, 0.0)
                            location.world.spawn(location, Firework::class.java)
                            index ++
                        }
                    }
                } else {
                    matchPlayer.life --
                    broadcast("&b[AcrobatSniper] &6$enemyName &fが &6$playerName &fの残機を減らし、残り &6${matchPlayer.life} &fになりました")
                    plugin.runTaskLater(3) {
                        matchPlayer.match.start()
                    }
                }
            }
            cancelEventIf<EntityShootBowEvent> {
                it.entity.hasPotionEffect(PotionEffectType.WEAKNESS)
            }
            cancelEventIf<PlayerMoveEvent> {
                it.player.hasPotionEffect(PotionEffectType.SLOW)
            }
        }
    }
}
