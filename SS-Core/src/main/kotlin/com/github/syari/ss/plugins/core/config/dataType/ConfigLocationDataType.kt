package com.github.syari.ss.plugins.core.config.dataType

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.config.CustomFileConfig
import org.bukkit.Bukkit
import org.bukkit.Location

object ConfigLocationDataType: ConfigDataType.WithSet<Location> {
    override val typeName = "Location"

    override fun get(
        config: CustomConfig, path: String, notFoundError: Boolean
    ): Location? {
        val line = config.get(path, ConfigDataType.STRING, notFoundError) ?: return null
        val split = line.split(",\\s*".toRegex())
        when (val size = split.size) {
            4, 6 -> {
                val world = Bukkit.getWorld(split[0])
                if (world == null) {
                    config.nullError(path, "World(${split[0]})")
                    return null
                }
                try {
                    val x = split[1].toDouble()
                    val y = split[2].toDouble()
                    val z = split[3].toDouble()
                    if (size == 4) return Location(world, x, y, z)
                    val yaw = split[4].toFloat()
                    val pitch = split[5].toFloat()
                    return Location(world, x, y, z, yaw, pitch)
                } catch (ex: NumberFormatException) {
                    config.formatMismatchError(path)
                    return null
                }
            }
            else -> {
                config.formatMismatchError(path)
                return null
            }
        }
    }

    override fun set(config: CustomFileConfig, path: String, value: Location?) {
        if (value != null) {
            config.set(
                path, ConfigDataType.STRING, if (value.yaw == 0F && value.pitch == 0F) {
                    "${value.world.name}, ${value.x}, ${value.y}, ${value.z}"
                } else {
                    "${value.world.name}, ${value.x}, ${value.y}, ${value.z}, ${value.yaw}, ${value.pitch}"
                }
            )
        } else {
            config.setUnsafe(path, null)
        }
    }
}