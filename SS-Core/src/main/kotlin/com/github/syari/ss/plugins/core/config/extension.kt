package com.github.syari.ss.plugins.core.config

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

@OptIn(ExperimentalStdlibApi::class)
fun Array<ItemStack?>.asInventoryMap() = buildMap<Int, ItemStack> {
    forEachIndexed { slot, item ->
        if (item != null && item.type != Material.AIR) {
            put(slot, item)
        }
    }
}
