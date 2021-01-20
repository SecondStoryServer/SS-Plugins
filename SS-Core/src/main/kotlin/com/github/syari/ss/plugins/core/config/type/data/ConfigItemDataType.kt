@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.config.type.data

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.config.type.ConfigDataType
import com.github.syari.ss.plugins.core.item.CustomItemStack

class ConfigItemDataType(private val itemConverter: ConfigItemConverter) : ConfigDataType<CustomItemStack> {
    override val typeName = "List<Item>"

    override fun get(
        config: CustomConfig,
        path: String,
        notFoundError: Boolean
    ): CustomItemStack? {
        val line = config.get(path, ConfigDataType.STRING, notFoundError) ?: return config.nullError(path, "String").run { null }
        return itemConverter.get(line) ?: config.nullError(path, itemConverter.things).run { null }
    }
}
