package com.github.syari.ss.plugins.demonkill.craft

import com.github.syari.ss.plugins.core.item.CustomItemStack
import org.bukkit.Material
import org.bukkit.entity.Player

open class DependItem(val item: CustomItemStack) {
    companion object {
        fun <T : DependItem> getFromInventory(list: Set<T>, player: Player): T? {
            player.inventory.forEach { itemStack ->
                val item = CustomItemStack.create(itemStack)
                if (item.type != Material.AIR) {
                    list.firstOrNull { it.item.type == item.type && it.item.display == item.display }?.let {
                        return it
                    }
                }
            }
            return null
        }
    }
}
