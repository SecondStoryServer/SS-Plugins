package com.github.syari.ss.plugins.mobarena.wave.boss

import org.bukkit.Location
import org.bukkit.entity.Entity

interface MobArenaBoss {
    fun spawn(location: Location): Entity?
}
