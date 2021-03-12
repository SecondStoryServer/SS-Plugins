package com.github.syari.ss.plugins.mobarena.lobby

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface LobbyItem {
    val item: ItemStack

    fun onClick(player: Player)
}
