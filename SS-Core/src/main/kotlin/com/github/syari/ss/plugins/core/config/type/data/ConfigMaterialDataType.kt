package com.github.syari.ss.plugins.core.config.type.data

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.config.CustomFileConfig
import com.github.syari.ss.plugins.core.config.type.ConfigDataType
import org.bukkit.Material

object ConfigMaterialDataType : ConfigDataType.WithSet<Material> {
    override val typeName = "Material"

    override fun get(
        config: CustomConfig,
        path: String,
        notFoundError: Boolean
    ): Material? {
        val getValue = config.get(path, ConfigDataType.STRING, notFoundError) ?: return null
        return Material.getMaterial(getValue)
    }

    override fun set(config: CustomFileConfig, path: String, value: Material?) {
        config.set(path, ConfigDataType.STRING, value?.name)
    }
}
