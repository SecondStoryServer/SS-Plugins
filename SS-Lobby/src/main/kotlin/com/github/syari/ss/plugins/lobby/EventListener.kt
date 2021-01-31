package com.github.syari.ss.plugins.lobby

import com.github.syari.ss.plugins.core.code.EventRegister
import com.github.syari.ss.plugins.core.code.ListenerFunctions
import com.github.syari.ss.plugins.core.item.CustomItemStack
import com.github.syari.ss.plugins.core.scheduler.CreateScheduler.runLater
import org.bukkit.GameMode
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent

object EventListener : EventRegister {
    override fun ListenerFunctions.events() {
        event<PlayerJoinEvent> {
            plugin.runLater(1) {
                LobbyInventory.applyToPlayer(it.player)
            }
        }
        event<PlayerDropItemEvent>(ignoreCancelled = true) {
            val player = it.player
            if (player.isOp.not() || player.gameMode != GameMode.CREATIVE) {
                it.isCancelled = true
            }
        }
        event<PlayerInteractEvent> { e ->
            val item = e.item
            LobbyInventory.inventory.values.firstOrNull {
                it.item.isSimilar(item)
            }?.let {
                it.toggle(e.player, CustomItemStack.create(item))
                e.isCancelled = true
            }
        }
    }
}
