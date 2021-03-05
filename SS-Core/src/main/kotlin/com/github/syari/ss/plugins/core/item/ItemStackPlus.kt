@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.item

import org.bukkit.Bukkit
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object ItemStackPlus {
    /**
     * プレイヤーにアイテムを渡します
     * @param item アイテム
     */
    fun HumanEntity.give(item: ItemStack) {
        inventory.addItem(item)
    }

    /**
     * プレイヤーにアイテムを渡します
     * @param items アイテム
     */
    fun HumanEntity.give(items: Iterable<ItemStack>) {
        items.forEach {
            give(it)
        }
    }

    /**
     * プレイヤーにアイテムを渡しますが、インベントリが一杯の場合はその場に落とします
     * @param item アイテム
     */
    fun HumanEntity.giveOrDrop(item: ItemStack) {
        if (inventory.firstEmpty() in 0 until 36) {
            give(item)
        } else {
            location.world.dropItem(location, item)
        }
    }

    /**
     * プレイヤーにアイテムを渡しますが、インベントリが一杯の場合はその場に落とします
     * @param items アイテム
     */
    fun HumanEntity.giveOrDrop(items: Iterable<ItemStack>) {
        items.forEach { item ->
            giveOrDrop(item)
        }
    }

    /**
     * プレイヤーがアイテムを持っているか判定します
     * @param item アイテム
     * @return [Boolean]
     */
    fun HumanEntity.hasItem(item: ItemStack, amount: Int): Boolean {
        return Bukkit.createInventory(null, InventoryType.PLAYER).apply {
            this.contents = inventory.contents
        }.removeItem(item, amount)
    }

    /**
     * プレイヤーからアイテムを取り除きます
     * @param item アイテム
     * @return [Boolean]
     */
    fun HumanEntity.removeItem(item: ItemStack, amount: Int): Boolean {
        return inventory.removeItem(item, amount)
    }

    /**
     * プレイヤーからアイテムを取り除きます
     * @param item アイテム
     * @return [Boolean]
     */
    fun Inventory.removeItem(item: ItemStack, amount: Int): Boolean {
        val contents = this.contents
        var remain = amount
        contents.forEach {
            if (it != null && it.type == item.type && it.damage == item.damage && (it.hasItemMeta() == item.hasItemMeta()) && it.displayName == item.displayName) {
                val a = it.amount
                if (a < remain) {
                    remain -= a
                    it.amount = 0
                } else {
                    it.amount = a - remain
                    this.contents = contents
                    return true
                }
            }
        }
        return false
    }
}
