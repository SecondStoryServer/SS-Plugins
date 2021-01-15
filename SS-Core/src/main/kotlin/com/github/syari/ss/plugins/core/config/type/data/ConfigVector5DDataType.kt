package com.github.syari.ss.plugins.core.config.type.data

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.config.CustomFileConfig
import com.github.syari.ss.plugins.core.config.type.ConfigDataType
import com.github.syari.ss.plugins.core.world.Vector5D

object ConfigVector5DDataType : ConfigDataType.WithSet<Vector5D> {
    override val typeName = "Vector5D"

    override fun get(
        config: CustomConfig,
        path: String,
        notFoundError: Boolean
    ): Vector5D? {
        val line = config.get(path, ConfigDataType.STRING, notFoundError) ?: return null
        val split = line.split(",\\s*".toRegex())
        return when (val size = split.size) {
            3, 5 -> {
                try {
                    val x = split[1].toDouble()
                    val y = split[2].toDouble()
                    val z = split[3].toDouble()
                    if (size == 3) return Vector5D(x, y, z)
                    val yaw = split[4].toFloat()
                    val pitch = split[5].toFloat()
                    Vector5D(x, y, z, yaw, pitch)
                } catch (ex: NumberFormatException) {
                    config.formatMismatchError(path)
                    null
                }
            }
            else -> {
                config.formatMismatchError(path)
                null
            }
        }
    }

    override fun set(config: CustomFileConfig, path: String, value: Vector5D?) {
        if (value != null) {
            config.set(
                path, ConfigDataType.STRING,
                if (value.yaw == 0F && value.pitch == 0F) {
                    "${value.x}, ${value.y}, ${value.z}"
                } else {
                    "${value.x}, ${value.y}, ${value.z}, ${value.yaw}, ${value.pitch}"
                }
            )
        } else {
            config.setUnsafe(path, null)
        }
    }
}
