package com.github.syari.ss.plugins.mobarena

import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack

inline val InventoryClickEvent.insertItem
    get(): ItemStack? {
        val player = whoClicked as? Player ?: return null
        return if (player.openInventory.topInventory.type != InventoryType.CRAFTING) {
            when {
                clickedInventory == player.inventory -> currentItem
                click == ClickType.NUMBER_KEY -> player.inventory.getItem(hotbarButton)
                else -> cursor
            }
        } else {
            null
        }
    }
