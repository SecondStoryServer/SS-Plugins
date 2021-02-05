package com.github.syari.ss.plugins.lobby

import com.github.syari.ss.plugins.core.code.CoolTime.Companion.coolTime
import com.github.syari.ss.plugins.core.code.EventRegister
import com.github.syari.ss.plugins.core.code.Events
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
    override fun Events.register() {
        event<PlayerJoinEvent> {
            plugin.runLater(1) {
                LobbyInventory.applyToPlayer(it.player)
            }
        }
        cancelEvent<PlayerDropItemEvent> {
            val player = it.player
            player.isOp.not() || player.gameMode != GameMode.CREATIVE
        }
        val gadgetCoolTime = plugin.coolTime<UUIDPlayer>()
        event<PlayerInteractEvent> { e ->
            val item = e.item ?: return@event
            e.isCancelled = true
            val player = e.player
            val uuidPlayer = UUIDPlayer(player)
            if (gadgetCoolTime.contains(uuidPlayer).not()) {
                LobbyInventory.inventory.values.firstOrNull {
                    it.item.itemMeta?.displayName == item.itemMeta?.displayName
                }?.let {
                    it.toggle(player, CustomItemStack.create(item))
                    gadgetCoolTime.add(uuidPlayer, 10)
                }
            }
        }
        cancelEvent<EntityAirChangeEvent> {
            it.entity is Player
        }
        cancelEvent<EntityCombustEvent> {
            it.entity is Player
        }
        cancelEvent<EntityDamageEvent> {
            it.entity is Player
        }
        cancelEvent<PlayerSwapHandItemsEvent> {
            true
        }
        cancelEvent<InventoryClickEvent> {
            val cancelSlots = 36..40
            it.isShiftClick || it.slot in cancelSlots || it.rawSlot in cancelSlots
        }
    }
}
