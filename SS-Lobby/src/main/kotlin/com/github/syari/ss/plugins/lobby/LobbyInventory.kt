package com.github.syari.ss.plugins.lobby

import com.github.syari.ss.plugins.lobby.gadget.DoubleJump
import com.github.syari.ss.plugins.lobby.gadget.Elytra
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object LobbyInventory {
    private val inventory = mapOf(
        7 to DoubleJump,
        8 to Elytra
    )

    fun getItem(item: ItemStack): LobbyItem? {
        return inventory.values.firstOrNull {
            it.item.itemMeta?.displayName == item.itemMeta?.displayName
        }
    }

    fun applyToPlayer(player: Player) {
        inventory.forEach { (index, lobbyItem) ->
            player.inventory.setItem(index, lobbyItem.item)
        }
    }
}
