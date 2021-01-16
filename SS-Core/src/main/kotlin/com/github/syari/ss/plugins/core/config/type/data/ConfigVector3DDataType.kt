@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.config.type.data

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.config.CustomFileConfig
import com.github.syari.ss.plugins.core.config.type.ConfigDataType
import com.github.syari.ss.plugins.core.world.Vector3D

object ConfigVector3DDataType : ConfigDataType.WithSet<Vector3D> {
    override val typeName = "Vector3D"

    override fun get(
        config: CustomConfig,
        path: String,
        notFoundError: Boolean
    ): Vector3D? {
        val line = config.get(path, ConfigDataType.STRING, notFoundError) ?: return null
        val split = line.split(",\\s*".toRegex())
        return when (split.size) {
            3 -> {
                try {
                    val x = split[1].toDouble()
                    val y = split[2].toDouble()
                    val z = split[3].toDouble()
                    Vector3D(x, y, z)
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

    override fun set(config: CustomFileConfig, path: String, value: Vector3D?) {
        if (value != null) {
            config.set(path, ConfigDataType.STRING, "${value.x}, ${value.y}, ${value.z}")
        } else {
            config.setUnsafe(path, null)
        }
    }
}
