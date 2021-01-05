package com.github.syari.ss.plugins.core.world

import org.bukkit.Location
import org.bukkit.World

data class Vector3D(
    val x: Double, val y: Double, val z: Double
) {
    /**
     * Location に変換します
     * @param world ワールド
     * @param yaw
     * @param pitch
     * @return [Location]
     */
    fun toLocation(world: World, yaw: Float, pitch: Float) = Location(world, x, y, z, yaw, pitch)

    /**
     * (X, Y, Z) のフォーマットで文字列を返します
     * @return [String]
     */
    override fun toString(): String {
        return "$x, $y, $z"
    }

    companion object {
        /**
         * Location から Vector5D に変換します
         * @param location [Location]
         * @return [Vector5D]
         */
        fun fromLocation(location: Location) = Vector3D(location.x, location.y, location.z)

        /**
         * String から Vector3D に変換します
         * @param string (X, Y, Z)
         * @return [Vector5D]
         */
        fun fromString(string: String): Vector3D? {
            val split = string.split(",\\s*".toRegex())
            when (split.size) {
                3 -> {
                    val x = split[0].toDoubleOrNull() ?: return null
                    val y = split[1].toDoubleOrNull() ?: return null
                    val z = split[2].toDoubleOrNull() ?: return null
                    return Vector3D(x, y, z)
                }
                else -> return null
            }
        }
    }
}