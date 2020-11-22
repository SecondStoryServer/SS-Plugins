package com.github.syari.ss.plugins.core.item

import org.bukkit.entity.HumanEntity
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
}