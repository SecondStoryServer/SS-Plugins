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
        event<InventoryClickEvent>(ignoreCancelled = true) { e ->
            val player = e.whoClicked as? Player ?: return@event
            if (player.inMobArena.not()) return@event
            if (CustomItemStack.create(e.insertItem).lore.contains("&c受け渡し不可".toColor)) {
                e.isCancelled = true
            }
        }
        event<PlayerQuitEvent> { e ->
            val player = e.player
            val arena = player.arena ?: return@event
            arena.leave(player)
        }
        event<PlayerDeathEvent> { e ->
            val player = e.entity
            val arena = player.arena ?: return@event
            e.deathMessage = null
            e.isCancelled = true
            arena.onDeath(player)
        }
        event<EntityDeathEvent> { e ->
            val entity = e.entity
            val arena = getArena(entity) ?: return@event
            e.droppedExp = 0
            e.drops.clear()
            arena.onKillEntity(entity)
        }
        event<PlayerInteractEvent> { e ->
            val player = e.player
            val block = e.clickedBlock ?: return@event
            val arena = player.arena ?: return@event
            e.isCancelled = true
            if (block.type == Material.CHEST && e.action == Action.RIGHT_CLICK_BLOCK) {
                arena.publicChest.open(player)
            }
        }
        cancelEvent<PlayerItemDamageEvent> { e ->
            e.player.inMobArena
        }
        event<EntityDamageByEntityEvent>(ignoreCancelled = true) { e ->
            val victim = e.entity
            val attacker = when (e.damager) {
                is Projectile -> (e.damager as Projectile).shooter as? Entity
                else -> e.damager
            }
            if (victim is Player) {
                if (attacker is Player && (victim.inMobArena || attacker.inMobArena)) {
                    e.isCancelled = true
                }
            } else if (attacker != null) {
                val victimArena = getArena(victim)
                if (victimArena != null && victimArena == getArena(attacker)) {
                    e.isCancelled = true
                }
            }
        }
        cancelEvent<PlayerDropItemEvent> { e ->
            e.player.inMobArena
        }
        cancelEvent<ItemSpawnEvent> { e ->
            getArenaInPlay(e.location) != null
        }
        cancelEvent<FoodLevelChangeEvent> { e ->
            val player = e.entity as Player
            player.inMobArena
        }
        event<EntityTargetEvent> { e ->
            val entity = e.entity
            val arena = getArena(entity) ?: return@event
            if (e.target !is Player) e.target = arena.livingPlayers.random().player
        }
        event<EntityCombustEvent>(ignoreCancelled = true) { e ->
            val entity = e.entity
            if (getArena(entity) == null) return@event
            if (e is EntityCombustByBlockEvent || e is EntityDamageByEntityEvent) {
                return@event
            }
            e.isCancelled = true
        }
    }
}
