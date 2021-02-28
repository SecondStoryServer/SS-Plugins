package com.github.syari.ss.plugins.demonkill.craft

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

open class DependItem(val item: ItemStack) {
    companion object {
        fun <T : DependItem> getFromInventory(list: Set<T>, player: Player): T? {
            player.inventory.forEach { item ->
                if (item.type != Material.AIR) {
                    list.firstOrNull { it.item.type == item.type && it.item.displayName == item.displayName }?.let {
                        return it
                    }
                }
            }
            return null
        }
    }
}
