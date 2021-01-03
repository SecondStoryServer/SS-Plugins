package com.github.syari.ss.plugins.ma.chest

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import su.nightexpress.ama.api.ArenaAPI
import su.nightexpress.ama.arena.api.events.objects.ArenaStartEvent

object EventListener: Listener {
    private val chests = mutableMapOf<String, ArenaChest>()

    @EventHandler
    fun on(e: PlayerInteractEvent) {
        if (e.clickedBlock?.type != Material.CHEST) return
        val player = e.player
        val arenaPlayer = ArenaAPI.getArenaManager().getArenaPlayer(player) ?: return
        e.isCancelled = true
        val chest = chests.getOrPut(arenaPlayer.arena.id) { ArenaChest() }
        chest.open(player)
    }

    @EventHandler
    fun on(e: ArenaStartEvent) {
        chests.remove(e.arena.id)
    }
}