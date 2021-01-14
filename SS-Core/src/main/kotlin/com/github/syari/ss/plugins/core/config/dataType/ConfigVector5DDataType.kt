package com.github.syari.ss.plugins.core.config.dataType

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.world.Vector5D

object ConfigVector5DDataType: ConfigDataType<Vector5D> {
    override val typeName = "Vector5D"

    override fun get(
        config: CustomConfig, path: String, notFoundError: Boolean
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

    fun toString(vector5D: Vector5D): String {
        return if (vector5D.yaw == 0F && vector5D.pitch == 0F) {
            "${vector5D.x}, ${vector5D.y}, ${vector5D.z}"
        } else {
            "${vector5D.x}, ${vector5D.y}, ${vector5D.z}, ${vector5D.yaw}, ${vector5D.pitch}"
        }
    }
}