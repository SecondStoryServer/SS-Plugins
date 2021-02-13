package com.github.syari.ss.plugins.demonkill.craft

import com.github.syari.ss.plugins.core.item.CustomItemStack
import org.bukkit.entity.Player

class Weapon(item: CustomItemStack, val upgrade: Map<CustomItemStack, Upgrade>) : Upgradeable(item) {
    companion object {
        var list = setOf<Weapon>()

        fun getFromInventory(player: Player): Weapon? {
            return getFromInventory(list, player)
        }
    }

    class Upgrade(override val request: List<CustomItemStack>) : Upgradeable.Upgrade
}
