package com.github.syari.ss.plugins.mobarena

import com.github.syari.ss.plugins.core.code.EventRegister
import com.github.syari.ss.plugins.core.code.ListenerFunctions
import com.github.syari.ss.plugins.core.code.StringEditor.toColor
import com.github.syari.ss.plugins.core.item.CustomItemStack
import com.github.syari.ss.plugins.mobarena.MobArenaManager.arena
import com.github.syari.ss.plugins.mobarena.MobArenaManager.getArena
import com.github.syari.ss.plugins.mobarena.MobArenaManager.getArenaInPlay
import com.github.syari.ss.plugins.mobarena.MobArenaManager.inMobArena
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityCombustByBlockEvent
import org.bukkit.event.entity.EntityCombustEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.entity.ItemSpawnEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemDamageEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack

object EventListener : EventRegister {
    private inline val InventoryClickEvent.insertItem
        get(): ItemStack? {
            val player = whoClicked as? Player ?: return null
            return if (player.openInventory.topInventory.type != InventoryType.CRAFTING) {
                when {
                    clickedInventory == player.inventory -> currentItem
                    click == ClickType.NUMBER_KEY -> player.inventory.getItem(hotbarButton)
                    else -> cursor
                }
            } else {
                null
            }
        }

    override fun ListenerFunctions.events() {
        event<InventoryClickEvent>(ignoreCancelled = true) {
            val player = it.whoClicked as? Player ?: return@event
            if (player.inMobArena.not()) return@event
            if (CustomItemStack.create(it.insertItem).lore.contains("&c受け渡し不可".toColor)) {
                it.isCancelled = true
            }
        }
        event<PlayerQuitEvent> {
            val player = it.player
            val arena = player.arena ?: return@event
            arena.leave(player)
        }
        event<PlayerDeathEvent> {
            val player = it.entity
            val arena = player.arena ?: return@event
            it.deathMessage = null
            it.isCancelled = true
            arena.onDeath(player)
        }
        event<EntityDeathEvent> {
            val entity = it.entity
            val arena = getArena(entity) ?: return@event
            it.droppedExp = 0
            it.drops.clear()
            arena.onKillEntity(entity)
        }
        event<PlayerInteractEvent> {
            val player = it.player
            val block = it.clickedBlock ?: return@event
            val arena = player.arena ?: return@event
            it.isCancelled = true
            if (block.type == Material.CHEST && it.action == Action.RIGHT_CLICK_BLOCK) {
                arena.publicChest.open(player)
            }
        }
        cancelEvent<PlayerItemDamageEvent> {
            it.player.inMobArena
        }
        event<EntityDamageByEntityEvent>(ignoreCancelled = true) {
            val victim = it.entity
            val attacker = when (it.damager) {
                is Projectile -> (it.damager as Projectile).shooter as? Entity
                else -> it.damager
            }
            if (victim is Player) {
                if (attacker is Player && (victim.inMobArena || attacker.inMobArena)) {
                    it.isCancelled = true
                }
            } else if (attacker != null) {
                val victimArena = getArena(victim)
                if (victimArena != null && victimArena == getArena(attacker)) {
                    it.isCancelled = true
                }
            }
        }
        cancelEvent<PlayerDropItemEvent> {
            it.player.inMobArena
        }
        cancelEvent<ItemSpawnEvent> {
            getArenaInPlay(it.location) != null
        }
        cancelEvent<FoodLevelChangeEvent> {
            val player = it.entity as Player
            player.inMobArena
        }
        event<EntityTargetEvent> {
            val entity = it.entity
            val arena = getArena(entity) ?: return@event
            if (it.target !is Player) it.target = arena.livingPlayers.random().player
        }
        event<EntityCombustEvent>(ignoreCancelled = true) {
            val entity = it.entity
            if (getArena(entity) == null) return@event
            if (it is EntityCombustByBlockEvent || it is EntityDamageByEntityEvent) {
                return@event
            }
            it.isCancelled = true
        }
    }
}
