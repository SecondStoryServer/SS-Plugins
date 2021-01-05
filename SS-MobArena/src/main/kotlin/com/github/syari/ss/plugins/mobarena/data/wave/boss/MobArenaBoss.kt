package com.github.syari.ss.plugins.mobarena.data.wave.boss

import com.github.syari.ss.plugins.mobarena.data.arena.MobArena
import org.bukkit.Location
import org.bukkit.entity.LivingEntity

interface MobArenaBoss {
    fun spawn(loc: Location, arena: MobArena): LivingEntity?
}