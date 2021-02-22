package com.github.syari.ss.plugins.event.acrobatsniper

import com.github.syari.spigot.api.util.uuid.UUIDPlayer
import org.bukkit.entity.Player

class MatchPlayer(val player: Player, val enemy: Player) {
    init {
        list[UUIDPlayer.from(player)] = this
    }

    lateinit var match: Match

    var life = maxLife

    companion object {
        private val list = mutableMapOf<UUIDPlayer, MatchPlayer>()

        fun get(player: Player) = list[UUIDPlayer.from(player)]

        fun remove(player: Player) {
            list.remove(UUIDPlayer.from(player))?.let {
                list.remove(UUIDPlayer.from(it.enemy))
            }
        }

        var maxLife = 1
    }
}
