@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.config.type.data

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.config.CustomFileConfig
import com.github.syari.ss.plugins.core.config.type.ConfigDataType
import org.bukkit.Material

object ConfigMaterialListDataType : ConfigDataType.WithSet<List<Material>> {
    override val typeName = "List<Material>"

    @OptIn(ExperimentalStdlibApi::class)
    override fun get(
        config: CustomConfig,
        path: String,
        notFoundError: Boolean
    ): List<Material> {
        return buildList {
            config.get(path, ConfigDataType.STRINGLIST, notFoundError)?.forEach {
                val type = Material.getMaterial(it.toUpperCase()) ?: return@forEach config.nullError("$path.$it", "Material")
                add(type)
            }
        }
    }

    override fun set(config: CustomFileConfig, path: String, value: List<Material>?) {
        config.set(path, ConfigDataType.STRINGLIST, value?.map(Material::name))
    }
}
