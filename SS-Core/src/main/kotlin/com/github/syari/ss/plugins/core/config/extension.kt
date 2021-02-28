@file:Suppress("FunctionName")

package com.github.syari.ss.plugins.core.config

import com.github.syari.spigot.api.config.type.ConfigDataType

fun ConfigDataType.Companion.Item(itemConverter: ConfigItemConverter) = ConfigItemDataType(itemConverter)
fun ConfigDataType.Companion.ItemList(itemConverter: ConfigItemConverter) = ConfigItemListDataType(itemConverter)
fun ConfigDataType.Companion.Inventory(itemConverter: ConfigItemConverter) = ConfigInventoryDataType(itemConverter)
