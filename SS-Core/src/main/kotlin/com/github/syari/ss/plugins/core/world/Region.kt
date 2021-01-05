package com.github.syari.ss.plugins.core.world

import org.bukkit.Location
import org.bukkit.World

class Region(val world: World, pos1: Vector3D, pos2: Vector3D) {
    companion object {
        fun fromNullable(world: World?, pos1: Vector3D?, pos2: Vector3D?): Region? {
            return if (world != null && pos1 != null && pos2 != null) Region(world, pos1, pos2) else null
        }

        fun fromNullable(pos1: Location?, pos2: Location?): Region? {
            return if (pos1 != null && pos2 != null) Region(pos1, pos2) else null
        }
    }

    constructor(pos1: Location, pos2: Location): this(pos1.world, Vector3D.fromLocation(pos1), Vector3D.fromLocation(pos2))

    val max: Vector3D
    val min: Vector3D

    init {
        val raw = Triple(
            if (pos1.x < pos2.x) {
                pos2.x to pos1.x
            } else {
                pos1.x to pos2.x
            }, if (pos1.y < pos2.y) {
                pos2.y to pos1.y
            } else {
                pos1.y to pos2.y
            }, if (pos1.z < pos2.z) {
                pos2.z to pos1.z
            } else {
                pos1.z to pos2.z
            }
        )
        max = Vector3D(raw.first.first, raw.second.first, raw.third.first)
        min = Vector3D(raw.first.second, raw.second.second, raw.third.second)
    }

    fun inRegion(location: Location): Boolean {
        return location.world == world && location.x in min.x..max.x && location.y in min.y..max.y && location.z in min.z..max.z
    }
}