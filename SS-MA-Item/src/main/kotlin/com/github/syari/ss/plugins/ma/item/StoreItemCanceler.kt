package com.github.syari.ss.plugins.ma.item

import com.github.syari.ss.plugins.core.code.StringEditor.toColor
import com.github.syari.ss.plugins.core.item.CustomItemStack
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack

object StoreItemCanceler: Listener {
    private inline val InventoryClickEvent.insertItem
        get(): ItemStack? {
            val p = whoClicked as? Player ?: return null
            return if (p.openInventory.topInventory.type != InventoryType.CRAFTING) {
                when {
                    clickedInventory == p.inventory -> currentItem
                    click == ClickType.NUMBER_KEY -> p.inventory.getItem(hotbarButton)
                    else -> cursor
                }
            } else {
                null
            }
        }

    @EventHandler(ignoreCancelled = true)
    fun on(e: InventoryClickEvent) {
        if (CustomItemStack.create(e.insertItem).lore.contains("&c受け渡し不可".toColor)) {
            e.isCancelled = true
        }
    }
}