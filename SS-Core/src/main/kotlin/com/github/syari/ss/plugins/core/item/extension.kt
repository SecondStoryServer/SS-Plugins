package com.github.syari.ss.plugins.core.item

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

fun itemStack(
    material: Material,
    displayName: String? = null,
    lore: List<String>? = null,
    customModelData: Int? = null,
    amount: Int = 1
): ItemStack {
    return ItemStack(material, amount).apply {
        setDisplayName(displayName)
        setLore(lore)
        setCustomModelData(customModelData)
    }
}

fun itemStack(
    material: Material,
    displayName: String? = null,
    vararg lore: String,
    customModelData: Int? = null,
    amount: Int = 1
): ItemStack {
    return itemStack(material, displayName, lore.toList(), customModelData, amount)
}
