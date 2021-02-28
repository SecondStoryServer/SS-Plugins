package com.github.syari.ss.plugins.demonkill.craft

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Armor(item: ItemStack, val upgrade: Map<ItemStack, Upgrade>) : Upgradeable(item) {
    companion object {
        var list = setOf<Armor>()

        fun getFromInventory(player: Player): Armor? {
            return getFromInventory(list, player)
        }
    }

    class Upgrade(val armor: Map<Int, ItemStack>, override val request: List<ItemStack>) : Upgradeable.Upgrade
}
