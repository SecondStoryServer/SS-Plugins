package com.github.syari.ss.plugins.core.item

import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack

object NBTItem {
    /**
     * アイテムから NBTTag を生成します
     */
    val ItemStack.nbtTag: String
        get() = CraftItemStack.asNMSCopy(this).orCreateTag.asString()
}
