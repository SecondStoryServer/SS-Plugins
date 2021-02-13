package com.github.syari.ss.plugins.demonkill.craft

import com.github.syari.ss.plugins.core.item.CustomItemStack
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Armor(item: CustomItemStack, val upgrade: Map<CustomItemStack, Upgrade>) : Upgradeable(item) {
    companion object {
        var list = setOf<Armor>()

        fun getFromInventory(player: Player): Armor? {
            return getFromInventory(list, player)
        }
    }

    class Upgrade(val armor: Map<Int, ItemStack>, override val request: List<CustomItemStack>) : Upgradeable.Upgrade
}
