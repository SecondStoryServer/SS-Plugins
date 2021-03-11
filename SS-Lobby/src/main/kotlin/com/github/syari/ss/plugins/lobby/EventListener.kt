package com.github.syari.ss.plugins.lobby

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent
import com.github.syari.spigot.api.event.EventRegister
import com.github.syari.spigot.api.event.Events
import com.github.syari.spigot.api.scheduler.runTaskLater
import com.github.syari.spigot.api.uuid.UUIDPlayer
import com.github.syari.ss.plugins.core.code.CoolTime.Companion.coolTime
import com.github.syari.ss.plugins.lobby.gadget.Gadget
import com.github.syari.ss.plugins.lobby.item.ClickableLobbyItem
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityAirChangeEvent
import org.bukkit.event.entity.EntityCombustEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent

object EventListener : EventRegister {
    override fun Events.register() {
        event<PlayerJoinEvent> {
            plugin.runTaskLater(1) {
                LobbyInventory.applyToPlayer(it.player)
            }
        }
        cancelEventIf<PlayerDropItemEvent> {
            val player = it.player
            player.isOp.not() || player.gameMode != GameMode.CREATIVE
        }
        val gadgetCoolTime = plugin.coolTime<UUIDPlayer>()
        event<PlayerInteractEvent> { e ->
            val item = e.item ?: return@event
            val player = e.player
            val uuidPlayer = UUIDPlayer.from(player)
            LobbyInventory.getItem(item)?.let {
                when (it) {
                    is Gadget -> {
                        if (gadgetCoolTime.contains(uuidPlayer).not()) {
                            it.toggle(player, item)
                            gadgetCoolTime.add(uuidPlayer, 10)
                        }
                    }
                    is ClickableLobbyItem -> {
                        it.onClick(player)
                    }
                }
                e.isCancelled = true
            }
        }
        event<PlayerElytraBoostEvent> {
            it.setShouldConsume(false)
        }
        cancelEventIf<EntityAirChangeEvent> {
            it.entity is Player
        }
        cancelEventIf<EntityCombustEvent> {
            it.entity is Player
        }
        cancelEventIf<EntityDamageEvent> {
            it.entity is Player
        }
        cancelEventIf<PlayerSwapHandItemsEvent> {
            true
        }
        cancelEventIf<InventoryClickEvent> {
            true
        }
    }
}
