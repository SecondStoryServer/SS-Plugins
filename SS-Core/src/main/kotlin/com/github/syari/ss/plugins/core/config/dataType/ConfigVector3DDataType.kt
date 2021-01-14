package com.github.syari.ss.plugins.core.config.dataType

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.world.Vector3D

object ConfigVector3DDataType: ConfigDataType<Vector3D> {
    override val typeName = "Vector3D"

    override fun get(
        config: CustomConfig, path: String, notFoundError: Boolean
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

    fun toString(vector3D: Vector3D): String {
        return "${vector3D.x}, ${vector3D.y}, ${vector3D.z}"
    }
}