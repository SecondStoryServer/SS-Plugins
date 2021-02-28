@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.config

import com.github.syari.ss.plugins.core.item.Base64Item
import org.bukkit.inventory.ItemStack

abstract class ConfigItemConverter(val things: String) {
    abstract fun get(line: String): ItemStack?

    @Suppress("FunctionName")
    companion object {
        val Base64 = object : ConfigItemConverter("CustomItemStack(Base64)") {
            override fun get(line: String) = Base64Item.fromBase64(line)
        }

        fun Format(typeMap: Map<String, (String, Int) -> ItemStack?>) = object : ConfigItemConverter("CustomItemStack(Format{${typeMap.keys.joinToString()}})") {
            override fun get(line: String): ItemStack? {
                val split = line.split("\\s+".toRegex())
                val id = split.getOrNull(1) ?: return null
                val amount = split.getOrNull(2)?.toIntOrNull() ?: 1
                return typeMap[split[0].toLowerCase()]?.invoke(id, amount)
            }
        }
    }
}
