package com.github.syari.ss.plugins.mobarena.data.arena

import com.github.syari.ss.plugins.mobarena.data.kit.MobArenaKit
import org.bukkit.Location
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

    var kitId: String? = null

    val kit: MobArenaKit?
        get() = MobArenaKit.getKit(kitId)

    fun isAllowMove(location: Location): Boolean {
        return if (play) {
            if (arena.status == MobArenaStatus.WaitReady) {
                arena.lobby.region.inRegion(location)
            } else {
                arena.play.region.inRegion(location)
            }
        } else {
            arena.spec.region.inRegion(location)
        }
    }
}