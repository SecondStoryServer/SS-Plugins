package com.github.syari.ss.plugins.mobarena.data.wave.mob

import org.bukkit.Location

class MobArenaMythicMobsMob(val id: String, override val priority: Int): MobArenaMob {
    override fun spawn(location: Location) = TODO() // = spawnMythicMobs(id, loc)?.livingEntity
}