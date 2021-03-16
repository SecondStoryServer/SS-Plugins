package com.github.syari.ss.plugins.core.item

import com.github.syari.spigot.api.item.itemStack
import org.bukkit.Material

fun itemStack(
    material: Material,
    displayName: String? = null,
    lore: List<String> = listOf(),
    customModelData: Int? = null,
    amount: Int = 1
) = itemStack(material, displayName, lore) {
    setAmount(amount)
    setCustomModelData(customModelData)
}

fun itemStack(
    material: Material,
    displayName: String? = null,
    vararg lore: String,
    customModelData: Int? = null,
    amount: Int = 1
) = itemStack(material, displayName, lore.toList(), customModelData, amount)
