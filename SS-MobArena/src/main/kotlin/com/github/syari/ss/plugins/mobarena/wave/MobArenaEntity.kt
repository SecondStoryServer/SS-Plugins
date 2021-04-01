package com.github.syari.ss.plugins.mobarena.wave

import org.bukkit.Location
import org.bukkit.entity.Entity

interface MobArenaEntity {
    fun spawn(location: Location): Entity?
}
