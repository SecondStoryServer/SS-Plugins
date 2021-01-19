@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.config.type.data

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.config.type.ConfigDataType
import com.github.syari.ss.plugins.core.config.type.ConfigSectionType
import org.bukkit.inventory.ItemStack

class ConfigInventoryDataType(private val itemConverter: ConfigItemConverter) : ConfigDataType<Map<Int, ItemStack>> {
    override val typeName = "Inventory"

    @OptIn(ExperimentalStdlibApi::class)
    override fun get(
        config: CustomConfig,
        path: String,
        notFoundError: Boolean
    ): Map<Int, ItemStack> {
        val itemDataType = ConfigDataType.ITEM(itemConverter)
        return buildMap {
            config.section(path, ConfigSectionType.INT, notFoundError)?.forEach {
                this[it] = config.get("$path.$it", itemDataType) ?: return@forEach
            }
        }
    }
}
