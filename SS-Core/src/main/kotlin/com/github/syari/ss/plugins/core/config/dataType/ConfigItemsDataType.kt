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
                val slotNumber = it.toIntOrNull() ?: return@forEach config.typeMismatchError("$path.$it", "Int")
                val line = config.get("$path.$it", ConfigDataType.STRING) ?: return@forEach config.nullError("$path.$it", "String")
                val item = itemConverter.get(line) ?: return@forEach config.nullError("$path.$it", itemConverter.things)
                this[slotNumber] = item
            }
        }
    }
}
