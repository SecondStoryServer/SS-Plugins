package com.github.syari.ss.plugins.core.config.dataType

import com.github.syari.ss.plugins.core.config.CustomConfig
import org.bukkit.Bukkit
import org.bukkit.World

object ConfigWorldDataType: ConfigDataType<World> {
    override val typeName = "World"

    override fun get(
        config: CustomConfig, path: String, notFoundError: Boolean
    ): World? {
        val getValue = config.get(path, ConfigDataType.STRING, notFoundError) ?: return null
        return Bukkit.getWorld(getValue)
    }
}