package com.github.syari.ss.plugins.mobarena.data.wave.mob

import org.bukkit.Location
import org.bukkit.entity.LivingEntity

interface MobArenaMob {
    val priority: Int

    fun spawn(location: Location): LivingEntity?
}