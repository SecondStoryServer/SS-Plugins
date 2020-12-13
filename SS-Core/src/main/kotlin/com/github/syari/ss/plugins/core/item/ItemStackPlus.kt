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
    fun HumanEntity.give(items: Iterable<ItemStack?>) {
        items.forEach {
            if (it != null) give(it)
        }
    }

    /**
     * プレイヤーにアイテムを渡します
     * @param item アイテム
     */
    fun HumanEntity.give(item: CustomItemStack) {
        give(item.toItemStack)
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
    fun HumanEntity.giveOrDrop(items: Iterable<ItemStack?>) {
        items.forEach { item ->
            if (item != null) giveOrDrop(item)
        }
    }

    /**
     * プレイヤーにアイテムを渡しますが、インベントリが一杯の場合はその場に落とします
     * @param item アイテム
     */
    fun HumanEntity.giveOrDrop(item: CustomItemStack) {
        giveOrDrop(item.toItemStack)
    }

    /**
     * プレイヤーがアイテムを持っているか判定します
     * @param item アイテム
     * @return [Boolean]
     */
    fun HumanEntity.hasItem(item: CustomItemStack): Boolean {
        return Bukkit.createInventory(null, InventoryType.PLAYER).apply {
            this.contents = inventory.contents
        }.removeItem(item)
    }

    /**
     * プレイヤーからアイテムを取り除きます
     * @param item アイテム
     * @return [Boolean]
     */
    fun HumanEntity.removeItem(item: CustomItemStack): Boolean {
        return inventory.removeItem(item)
    }

    /**
     * プレイヤーからアイテムを取り除きます
     * @param item アイテム
     * @return [Boolean]
     */
    fun Inventory.removeItem(item: CustomItemStack): Boolean {
        val contents = this.contents
        var amount = item.amount
        contents.forEach { i ->
            val ci = CustomItemStack.create(i)
            if (ci.type == item.type && ci.damage == item.damage && (ci.hasItemMeta == item.hasItemMeta) && ci.display == item.display) {
                val a = i.amount
                if (a < amount) {
                    amount -= a
                    i.amount = 0
                } else {
                    i.amount = a - amount
                    this.contents = contents
                    return true
                }
            }
        }
        return false
    }
}