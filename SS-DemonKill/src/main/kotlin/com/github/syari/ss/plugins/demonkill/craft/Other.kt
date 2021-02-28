package com.github.syari.ss.plugins.demonkill.craft

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Other(item: ItemStack, val list: Map<Int, Pair<ItemStack, List<ItemStack>>>) : DependItem(item) {
    companion object {
        var list = setOf<Other>()

        fun getFromInventory(player: Player): Other? {
            return getFromInventory(list, player)
        }
    }
}
