package com.github.syari.ss.plugins.mobarena.player

import com.github.syari.spigot.api.config.config
import com.github.syari.spigot.api.config.type.ConfigDataType
import com.github.syari.spigot.api.uuid.UUIDPlayer
import com.github.syari.ss.plugins.core.console
import com.github.syari.ss.plugins.mobarena.Main.Companion.plugin
import org.bukkit.entity.Player

data class PlayerData(val uuidPlayer: UUIDPlayer) {
    private val file = plugin.config(console, "Player/$uuidPlayer.yml")

    var coin = file.get("coin", ConfigDataType.Int, 0, false)
        set(value) {
            file.set("coin", ConfigDataType.Int, value, true)
            field = value
        }

    var kit = file.get("kit", ConfigDataType.StringList, listOf(), false)
        set(value) {
            file.set("kit", ConfigDataType.StringList, value, true)
            field = value
        }

    companion object {
        private val list = mutableMapOf<UUIDPlayer, PlayerData>()

        fun get(uuidPlayer: UUIDPlayer) = list.getOrPut(uuidPlayer) { PlayerData(uuidPlayer) }

        fun get(player: Player) = get(UUIDPlayer.from(player))
    }
}
