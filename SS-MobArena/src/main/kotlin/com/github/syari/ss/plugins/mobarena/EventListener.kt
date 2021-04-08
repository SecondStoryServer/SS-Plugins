package com.github.syari.ss.plugins.mobarena

import com.github.syari.spigot.api.event.events
import com.github.syari.spigot.api.scheduler.runTaskLater
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.mobarena.Main.Companion.plugin
import com.github.syari.ss.plugins.mobarena.lobby.LobbyInventory
import org.bukkit.GameMode
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemDamageEvent
import org.bukkit.event.player.PlayerJoinEvent

object EventListener : OnEnable {
    override fun onEnable() {
        plugin.events {
            event<PlayerJoinEvent> {
                plugin.runTaskLater(1) {
                    LobbyInventory.applyToPlayer(it.player)
                }
            }
            event<PlayerInteractEvent> { e ->
                e.item?.let(LobbyInventory::getItem)?.let {
                    it.onClick(e.player)
                    e.isCancelled = true
                }
            }
            cancelEventIf<PlayerDropItemEvent> {
                val player = it.player
                player.isOp.not() || player.gameMode != GameMode.CREATIVE
            }
            cancelEventIf<FoodLevelChangeEvent> {
                true
            }
            cancelEventIf<PlayerItemDamageEvent> {
                true
            }
        }
    }
}
