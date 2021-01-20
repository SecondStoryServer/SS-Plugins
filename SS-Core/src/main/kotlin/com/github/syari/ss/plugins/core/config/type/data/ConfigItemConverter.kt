@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.config.type.data

import com.github.syari.ss.plugins.core.item.Base64Item
import com.github.syari.ss.plugins.core.item.CustomItemStack

abstract class ConfigItemConverter(val things: String) {
    abstract fun get(line: String): CustomItemStack?

    @Suppress("FunctionName")
    companion object {
        val Base64 = object : ConfigItemConverter("CustomItemStack(Base64)") {
            override fun get(line: String) = CustomItemStack.fromNullable(Base64Item.fromBase64(line))
        }

        fun Format(typeMap: Map<String, (String, Int) -> CustomItemStack?>) = object : ConfigItemConverter("CustomItemStack(Format{${typeMap.keys.joinToString()}})") {
            override fun get(line: String): CustomItemStack? {
                val split = line.split("\\s+".toRegex())
                val id = split.getOrNull(1) ?: return null
                val amount = split.getOrNull(2)?.toIntOrNull() ?: 1
                return typeMap[split[0].toLowerCase()]?.invoke(id, amount)
            }
        }
    }
}
