package com.github.syari.ss.plugins.mobarena.data.kit

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class MobArenaKit(val id: String, val name: String, val items: Map<Int, ItemStack>) {
    companion object {
        var kits = mapOf<String, MobArenaKit>()

        fun getKit(id: String?) = kits[id]
    }

    fun load(player: Player) {

    }
}