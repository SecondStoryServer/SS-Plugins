@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.config

import com.github.syari.spigot.api.config.CustomConfig
import com.github.syari.spigot.api.config.type.ConfigDataType
import com.github.syari.spigot.api.config.type.ConfigSectionType
import org.bukkit.inventory.ItemStack

class ConfigInventoryDataType(private val itemConverter: ConfigItemConverter) : ConfigDataType<Map<Int, ItemStack>> {
    override val typeName = "Inventory"

    @OptIn(ExperimentalStdlibApi::class)
    override fun get(
        config: CustomConfig,
        path: String,
        notFoundError: Boolean
    ): Map<Int, ItemStack> {
        val itemDataType = ConfigDataType.Item(itemConverter)
        return buildMap {
            config.section(path, ConfigSectionType.Int, notFoundError)?.forEach {
                this[it] = config.get("$path.$it", itemDataType) ?: return@forEach
            }
        }
    }

    override fun set(config: CustomConfig, path: String, value: Map<Int, ItemStack>?) {
        throw UnsupportedOperationException()
    }
}
