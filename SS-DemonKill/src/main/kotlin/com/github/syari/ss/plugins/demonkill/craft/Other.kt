package com.github.syari.ss.plugins.demonkill.craft

import com.github.syari.ss.plugins.core.item.CustomItemStack
import org.bukkit.entity.Player

class Other(item: CustomItemStack, val list: Map<Int, Pair<CustomItemStack, List<CustomItemStack>>>) : DependItem(item) {
    companion object {
        var list = setOf<Other>()

        fun getFromInventory(player: Player): Other? {
            return getFromInventory(list, player)
        }
    }
}
