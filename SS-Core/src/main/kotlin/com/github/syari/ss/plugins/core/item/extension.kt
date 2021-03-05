package com.github.syari.ss.plugins.core.item

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import com.github.syari.spigot.api.util.item.displayName as eDisplayName
import com.github.syari.spigot.api.util.item.lore as eLore

fun itemStack(
    material: Material,
    displayName: String? = null,
    lore: List<String>? = null,
    customModelData: Int? = null,
    amount: Int = 1
): ItemStack {
    return ItemStack(material, amount).apply {
        eDisplayName = displayName
        eLore = lore.orEmpty()
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
