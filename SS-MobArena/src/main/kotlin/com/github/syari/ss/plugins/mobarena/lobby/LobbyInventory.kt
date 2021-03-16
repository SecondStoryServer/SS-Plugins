package com.github.syari.ss.plugins.mobarena.lobby

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object LobbyInventory {
    private val inventory = mapOf(
        0 to ArenaSelector,
        8 to ServerSelector
    )

    fun getItem(item: ItemStack): LobbyItem? {
        return inventory.values.firstOrNull {
            it.item.displayName == item.displayName
        }
    }

    fun applyToPlayer(player: Player) {
        inventory.forEach { (index, lobbyItem) ->
            player.inventory.setItem(index, lobbyItem.item)
        }
    }
}
