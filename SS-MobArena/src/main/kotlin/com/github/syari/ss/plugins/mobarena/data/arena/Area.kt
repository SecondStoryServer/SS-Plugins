package com.github.syari.ss.plugins.mobarena.data.arena

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.config.CustomFileConfig
import com.github.syari.ss.plugins.core.world.Region
import org.bukkit.Location
import com.github.syari.ss.plugins.core.config.dataType.ConfigDataType as IConfigDataType

class Area(val spawn: Location, val region: Region) {
    object ConfigDataType : IConfigDataType.WithSet<Area> {
        override val typeName = "Area"

        override fun get(config: CustomConfig, path: String, notFoundError: Boolean): Area? {
            val spawn = config.get("$path.spawn", IConfigDataType.LOCATION, notFoundError) ?: return run {
                config.nullError("$path.spawn", "Location")
                null
            }
            val pos1 = config.get("$path.pos1", IConfigDataType.LOCATION, notFoundError) ?: return run {
                config.nullError("$path.pos1", "Location")
                null
            }
            val pos2 = config.get("$path.pos2", IConfigDataType.LOCATION, notFoundError) ?: return run {
                config.nullError("$path.pos2", "Location")
                null
            }
            val region = Region(pos1, pos2)
            return Area(spawn, region)
        }

        override fun set(config: CustomFileConfig, path: String, value: Area?) {
            if (value != null) {
                config.set("$path.spawn", IConfigDataType.LOCATION, value.spawn)
                val minLocation = value.region.min.toLocation(value.region.world, 0F, 0F)
                config.set("$path.pos1", IConfigDataType.LOCATION, minLocation)
                val maxLocation = value.region.max.toLocation(value.region.world, 0F, 0F)
                config.set("$path.pos2", IConfigDataType.LOCATION, maxLocation)
            } else {
                config.setUnsafe(path, null)
            }
        }
    }
}
