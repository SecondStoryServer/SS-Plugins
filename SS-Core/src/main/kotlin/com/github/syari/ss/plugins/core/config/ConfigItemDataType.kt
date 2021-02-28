@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.config

import com.github.syari.spigot.api.config.CustomConfig
import com.github.syari.spigot.api.config.type.ConfigDataType
import com.github.syari.ss.plugins.core.item.CustomItemStack

class ConfigItemDataType(private val itemConverter: ConfigItemConverter) : ConfigDataType<CustomItemStack> {
    override val typeName = "List<Item>"

    override fun get(
        config: CustomConfig,
        path: String,
        notFoundError: Boolean
    ): CustomItemStack? {
        val line = config.get(path, ConfigDataType.String, notFoundError) ?: return config.nullError(path, "String").run { null }
        return itemConverter.get(line) ?: config.nullError(path, itemConverter.things).run { null }
    }

    override fun set(config: CustomConfig, path: String, value: CustomItemStack?) {
        throw UnsupportedOperationException()
    }
}
