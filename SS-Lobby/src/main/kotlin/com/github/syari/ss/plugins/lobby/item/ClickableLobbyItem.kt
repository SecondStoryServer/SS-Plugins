package com.github.syari.ss.plugins.lobby.item

import com.github.syari.ss.plugins.lobby.LobbyItem
import org.bukkit.entity.Player

interface ClickableLobbyItem : LobbyItem {
    fun onClick(player: Player)
}
