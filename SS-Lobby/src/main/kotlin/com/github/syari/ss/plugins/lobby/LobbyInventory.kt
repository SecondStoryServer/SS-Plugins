package com.github.syari.ss.plugins.lobby

import com.github.syari.ss.plugins.lobby.gadget.DoubleJump
import com.github.syari.ss.plugins.lobby.gadget.Elytra
import org.bukkit.entity.Player

object LobbyInventory {
    val inventory = mapOf(
        7 to DoubleJump,
        8 to Elytra
    )

    fun applyToPlayer(player: Player) {
        inventory.forEach { (index, lobbyItem) ->
            player.inventory.setItem(index, lobbyItem.item)
        }
    }
}
