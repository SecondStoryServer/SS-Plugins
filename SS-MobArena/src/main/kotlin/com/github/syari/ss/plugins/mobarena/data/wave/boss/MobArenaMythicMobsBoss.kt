package com.github.syari.ss.plugins.mobarena.data.wave.boss

import com.github.syari.ss.plugins.mobarena.data.arena.MobArena
import org.bukkit.Location

class MobArenaMythicMobsBoss(val id: String): MobArenaBoss {
    override fun spawn(loc: Location, arena: MobArena) = TODO() // = spawnMythicMobs(id, loc)?.livingEntity
}