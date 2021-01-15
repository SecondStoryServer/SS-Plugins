package com.github.syari.ss.plugins.core.config.dataType

import com.github.syari.ss.plugins.core.config.CustomConfig
import org.bukkit.inventory.ItemStack

class ConfigItemsDataType(private val itemConverter: ConfigItemConverter) : ConfigDataType<List<ItemStack>> {
    override val typeName = "Items"

    @OptIn(ExperimentalStdlibApi::class)
    override fun get(
        config: CustomConfig,
        path: String,
        notFoundError: Boolean
    ): List<ItemStack> {
        return buildList {
            config.get(path, ConfigDataType.STRINGLIST, notFoundError)?.forEach {
                val item = itemConverter.get(it) ?: return@forEach config.nullError("$path.$it", itemConverter.things)
                add(item)
            }
        }
    }
}
