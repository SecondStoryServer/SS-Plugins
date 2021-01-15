package com.github.syari.ss.plugins.core.config.type.data

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.config.type.ConfigDataType
import org.bukkit.inventory.ItemStack

class ConfigInventoryDataType(private val itemConverter: ConfigItemConverter) : ConfigDataType<Map<Int, ItemStack>> {
    override val typeName = "Inventory"

    @OptIn(ExperimentalStdlibApi::class)
    override fun get(
        config: CustomConfig,
        path: String,
        notFoundError: Boolean
    ): Map<Int, ItemStack> {
        return buildMap {
            config.section(path, notFoundError)?.forEach {
                val slotNumber = it.toIntOrNull() ?: return@forEach config.typeMismatchError("$path.$it", "Int")
                val line = config.get("$path.$it", ConfigDataType.STRING) ?: return@forEach config.nullError("$path.$it", "String")
                val item = itemConverter.get(line) ?: return@forEach config.nullError("$path.$it", itemConverter.things)
                this[slotNumber] = item
            }
        }
    }
}
