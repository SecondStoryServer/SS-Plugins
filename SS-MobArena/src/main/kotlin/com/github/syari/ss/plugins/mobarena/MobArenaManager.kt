package com.github.syari.ss.plugins.mobarena

import com.github.syari.spigot.api.uuid.UUIDEntity
import com.github.syari.ss.plugins.core.code.OnDisable
import com.github.syari.ss.plugins.mobarena.arena.MobArena
import com.github.syari.ss.plugins.mobarena.arena.MobArenaPlayer
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

object MobArenaManager : OnDisable {
    override fun onDisable() {
        endAllArena()
    }

    var arenas = listOf<MobArena>()
        set(value) {
            arenas.forEach { it.end(true) }
            field = value
        }

    val Player.inMobArena get() = arena != null

    val Player.arenaPlayer: MobArenaPlayer?
        get() {
            arenas.forEach {
                return it.getPlayer(this) ?: return@forEach
            }
            return null
        }

    inline val Player.arena
        get() = arenaPlayer?.arena

    fun getArena(id: String) = arenas.firstOrNull { it.id.equals(id, ignoreCase = true) }

    fun getArena(entity: Entity) = arenas.firstOrNull { UUIDEntity.from(entity) in it.mobs.keys }

    fun getArenaInPlay(loc: Location) = arenas.firstOrNull { it.playArea.region.inRegion(loc) }

    fun endAllArena() {
        arenas.forEach {
            it.end(true)
        }
    }
}
