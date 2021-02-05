package com.github.syari.ss.plugins.lobby

import com.github.syari.ss.plugins.core.code.CoolTime
import com.github.syari.ss.plugins.core.code.EventRegister
import com.github.syari.ss.plugins.core.code.ListenerFunctions
import com.github.syari.ss.plugins.core.item.CustomItemStack
import com.github.syari.ss.plugins.core.player.UUIDPlayer
import com.github.syari.ss.plugins.core.scheduler.CreateScheduler.runLater
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
        val gadgetCoolTime = CoolTime<UUIDPlayer>(plugin)
        event<PlayerInteractEvent> { e ->
            val item = e.item ?: return@event
            e.isCancelled = true
            val player = e.player
            val uuidPlayer = UUIDPlayer(player)
            if (gadgetCoolTime.isAvailable(uuidPlayer)) {
                LobbyInventory.inventory.values.firstOrNull {
                    it.item.itemMeta?.displayName == item.itemMeta?.displayName
                }?.let {
                    it.toggle(player, CustomItemStack.create(item))
                    gadgetCoolTime.add(uuidPlayer, 10)
                }
            }
        }
        event<EntityAirChangeEvent>(ignoreCancelled = true) {
            if (it.entity is Player) {
                it.isCancelled = true
            }
        }
        event<EntityCombustEvent>(ignoreCancelled = true) {
            if (it.entity is Player) {
                it.isCancelled = true
            }
        }
        event<EntityDamageEvent>(ignoreCancelled = true) {
            if (it.entity is Player) {
                it.isCancelled = true
            }
        }
        event<PlayerSwapHandItemsEvent>(ignoreCancelled = true) {
            it.isCancelled = true
        }
        event<InventoryClickEvent>(ignoreCancelled = true) {
            val cancelSlots = 36..40
            if (it.isShiftClick || it.slot in cancelSlots || it.rawSlot in cancelSlots) {
                it.isCancelled = true
            }
        }
    }
}
