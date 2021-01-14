package com.github.syari.ss.plugins.playerdatastore

import com.github.syari.ss.plugins.core.scheduler.CreateScheduler.runLater
import com.github.syari.ss.plugins.playerdatastore.Main.Companion.plugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object EventListener : Listener {
    @EventHandler
    fun on(e: PlayerJoinEvent) {
        plugin.runLater(3) {
            PlayerData.loadStoreData(e.player)
        }
    }

    @EventHandler
    fun on(e: PlayerQuitEvent) {
        PlayerData.saveStoreData(e.player)
    }
}
