@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.config.type.data

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.config.type.ConfigDataType
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
            config.get(path, ConfigDataType.STRINGLIST, notFoundError)?.forEach {
                val item = itemConverter.get(it) ?: return@forEach config.nullError("$path.$it", itemConverter.things)
                add(item)
            }
        }
    }
}
