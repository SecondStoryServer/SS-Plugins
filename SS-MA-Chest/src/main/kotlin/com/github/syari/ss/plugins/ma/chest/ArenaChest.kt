package com.github.syari.ss.plugins.ma.chest

import org.bukkit.Bukkit
import org.bukkit.entity.Player

class ArenaChest {
    private val inventory = Bukkit.createInventory(null, 54)

    fun open(player: Player) {
        player.openInventory(inventory)
    }
}