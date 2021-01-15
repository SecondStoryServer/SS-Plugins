package com.github.syari.ss.plugins.mobarena.wave.mob

import org.bukkit.Location
import org.bukkit.entity.Entity

interface MobArenaMob {
    val priority: Int

    fun spawn(location: Location): Entity?
}
