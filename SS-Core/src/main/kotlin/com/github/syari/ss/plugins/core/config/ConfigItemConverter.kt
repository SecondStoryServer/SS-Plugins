package com.github.syari.ss.plugins.core.config

import com.github.syari.spigot.api.config.CustomConfig
import com.github.syari.ss.plugins.core.item.Base64Item
import org.bukkit.inventory.ItemStack
import com.github.syari.spigot.api.config.converter.ConfigItemConverter as IConfigItemConverter

@Suppress("FunctionName")
object ConfigItemConverter {
    val Base64 = object : IConfigItemConverter {
        override fun stringToItem(config: CustomConfig, path: String, line: String): ItemStack? {
            return Base64Item.fromBase64(line) ?: config.nullError(path, "ItemStack(Base64)").run { null }
        }

        override fun itemToString(itemStack: ItemStack): String? {
            return Base64Item.toBase64(itemStack)
        }
    }

    fun FromName(typeMap: Map<String, (String) -> ItemStack?>) = object : IConfigItemConverter {
        private val types = typeMap.keys

        override fun stringToItem(config: CustomConfig, path: String, line: String): ItemStack? {
            val split = line.split("\\s+".toRegex())
            val type = split[0].toLowerCase()
            val nameToItem = typeMap[type] ?: return config.sendError(path, "$type というアイテムソースは存在しません (${types.joinToString()})").run { null }
            val name = split.getOrNull(1) ?: return config.formatMismatchError(path).run { null }
            val amount = split.getOrNull(2)?.toIntOrNull() ?: 1
            return nameToItem(name)?.asQuantity(amount) ?: config.sendError(path, "$type に $name というアイテムは存在しません").run { null }
        }

        override fun itemToString(itemStack: ItemStack): String? {
            return null
        }
    }
}
