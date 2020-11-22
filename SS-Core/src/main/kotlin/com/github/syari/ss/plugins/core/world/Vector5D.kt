package com.github.syari.ss.plugins.core.world

import org.bukkit.Location
import org.bukkit.World

data class Vector5D(
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float = 0F,
    val pitch: Float = 0F
) {
    /**
     * Location に変換します
     * @param world ワールド
     * @return [Location]
     */
    fun toLocation(world: World) = Location(world, x, y, z, yaw, pitch)

    /**
     * (X, Y, Z) or (X, Y, Z, yaw, pitch) のフォーマットで文字列を返します
     * @return [String]
     */
    override fun toString(): String {
        return if (yaw == 0F && pitch == 0F) "$x, $y, $z" else "$x, $y, $z, $yaw, $pitch"
    }

    companion object {
        /**
         * Location から Vector5D に変換します
         * @param location [Location]
         * @return [Vector5D]
         */
        fun fromLocation(location: Location) =
                Vector5D(location.x, location.y, location.z, location.yaw, location.pitch)

        /**
         * String から Vector5D に変換します
         * @param string (X, Y, Z) or (X, Y, Z, yaw, pitch)
         * @return [Vector5D]
         */
        fun fromString(string: String?): Vector5D? {
            if (string == null) return null
            val split = string.split(",\\s*".toRegex())
            when (val size = split.size) {
                3, 5 -> {
                    val x = split[0].toDoubleOrNull() ?: return null
                    val y = split[1].toDoubleOrNull() ?: return null
                    val z = split[2].toDoubleOrNull() ?: return null
                    if (size == 3) return Vector5D(x, y, z)
                    val yaw = split[3].toFloatOrNull() ?: return null
                    val pitch = split[4].toFloatOrNull() ?: return null
                    return Vector5D(x, y, z, yaw, pitch)
                }
                else -> return null
            }
        }
    }
}