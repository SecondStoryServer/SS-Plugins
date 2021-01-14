package com.github.syari.ss.plugins.mobarena.data.wave.boss

import com.github.syari.ss.plugins.dependency.mythicmobs.MythicMobsAPI.spawnMythicMobs
import org.bukkit.Location

class MobArenaMythicMobsBoss(val id: String) : MobArenaBoss {
    override fun spawn(location: Location) = spawnMythicMobs(id, location)?.entity?.bukkitEntity
}
