package com.github.syari.ss.plugins.playerdatastore

import com.github.syari.ss.plugins.assist.SpawnOverride
import com.github.syari.ss.plugins.core.code.EventRegister
import com.github.syari.ss.plugins.core.code.ListenerFunctions
import com.github.syari.ss.plugins.core.scheduler.CreateScheduler.runLater
import com.github.syari.ss.plugins.playerdatastore.PlayerData.Companion.storeData
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object EventListener : EventRegister {
    override fun ListenerFunctions.events() {
        event<PlayerJoinEvent> {
            plugin.runLater(3) {
                PlayerData.loadStoreData(it.player)
            }
        }
        event<PlayerQuitEvent> {
            PlayerData.saveStoreData(it.player)
        }
        event<SpawnOverride.JoinLocationOverrideEvent>(ignoreCancelled = true) {
            if (it.player.storeData.isEnableLocation) {
                it.isCancelled = true
            }
        }
    }
}
