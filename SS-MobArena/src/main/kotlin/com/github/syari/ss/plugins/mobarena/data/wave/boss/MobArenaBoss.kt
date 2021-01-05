package com.github.syari.ss.plugins.mobarena.data.wave.boss

import org.bukkit.Location
import org.bukkit.entity.Entity

interface MobArenaBoss {
    fun spawn(location: Location): Entity?
}