package com.github.syari.ss.plugins.demonkill.craft

import org.bukkit.inventory.ItemStack

open class Upgradeable(item: ItemStack) : DependItem(item) {
    interface Upgrade {
        val request: List<ItemStack>
    }
}
