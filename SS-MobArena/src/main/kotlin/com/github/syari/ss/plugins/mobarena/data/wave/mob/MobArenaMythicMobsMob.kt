package com.github.syari.ss.plugins.mobarena.data.wave.mob

import com.github.syari.ss.plugins.dependency.mythicmobs.MythicMobsAPI.spawnMythicMobs
import org.bukkit.Location

class MobArenaMythicMobsMob(val id: String, override val priority: Int) : MobArenaMob {
    override fun spawn(location: Location) = spawnMythicMobs(id, location)?.entity?.bukkitEntity
}
