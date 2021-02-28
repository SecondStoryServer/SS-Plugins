package com.github.syari.ss.plugins.demonkill.craft

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Weapon(item: ItemStack, val upgrade: Map<ItemStack, Upgrade>) : Upgradeable(item) {
    companion object {
        var list = setOf<Weapon>()

        fun getFromInventory(player: Player): Weapon? {
            return getFromInventory(list, player)
        }
    }

    class Upgrade(override val request: List<ItemStack>) : Upgradeable.Upgrade
}
