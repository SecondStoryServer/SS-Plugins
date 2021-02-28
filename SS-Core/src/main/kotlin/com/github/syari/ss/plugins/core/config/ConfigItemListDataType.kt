@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.config

import com.github.syari.spigot.api.config.CustomConfig
import com.github.syari.spigot.api.config.type.ConfigDataType
import com.github.syari.ss.plugins.core.item.CustomItemStack

class ConfigItemListDataType(private val itemConverter: ConfigItemConverter) : ConfigDataType<List<CustomItemStack>> {
    override val typeName = "List<Item>"

    @OptIn(ExperimentalStdlibApi::class)
    override fun get(
        config: CustomConfig,
        path: String,
        notFoundError: Boolean
    ): List<CustomItemStack> {
        return buildList {
            config.get(path, ConfigDataType.StringList, notFoundError)?.forEach {
                val item = itemConverter.get(it) ?: return@forEach config.nullError("$path.$it", itemConverter.things)
                add(item)
            }
        }
    }

    override fun set(config: CustomConfig, path: String, value: List<CustomItemStack>?) {
        throw UnsupportedOperationException()
    }
}
