package com.github.syari.ss.plugins.mobarena.arena

import com.github.syari.ss.plugins.mobarena.kit.MobArenaKit
import org.bukkit.entity.Player

class MobArenaPlayer(val arena: MobArena, val player: Player, var play: Boolean) {
    var ready = false
        set(value) {
            field = value
            if (value) {
                arena.checkReady(player)
            } else {
                arena.announce("&b[MobArena] &a${player.displayName}&fが準備完了を取り消しました")
            }
        }

    var kit: MobArenaKit? = null

    fun loadKit(kit: MobArenaKit) {
        arena.loadKit(this, kit)
    }
}
