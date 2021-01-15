package com.github.syari.ss.plugins.mobarena

import com.github.syari.ss.plugins.core.code.StringEditor.toColor
import com.github.syari.ss.plugins.core.item.CustomItemStack
import com.github.syari.ss.plugins.mobarena.MobArenaManager.arena
import com.github.syari.ss.plugins.mobarena.MobArenaManager.arenaPlayer
import com.github.syari.ss.plugins.mobarena.MobArenaManager.getArena
import com.github.syari.ss.plugins.mobarena.MobArenaManager.getArenaInPlay
import com.github.syari.ss.plugins.mobarena.MobArenaManager.inMobArena
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
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
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack

object EventListener : Listener {
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

    @EventHandler(ignoreCancelled = true)
    fun on(e: InventoryClickEvent) {
        val player = e.whoClicked as? Player ?: return
        if (player.inMobArena.not()) return
        if (CustomItemStack.create(e.insertItem).lore.contains("&c受け渡し不可".toColor)) {
            e.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun on(e: PlayerMoveEvent) {
        val player = e.player
        val arenaPlayer = player.arenaPlayer ?: return
        if (arenaPlayer.isAllowMove(e.to).not()) e.isCancelled = true
    }

    @EventHandler
    fun on(e: PlayerQuitEvent) {
        val player = e.player
        val arena = player.arena ?: return
        arena.leave(player)
    }

    @EventHandler
    fun on(e: PlayerDeathEvent) {
        val player = e.entity
        val arena = player.arena ?: return
        e.deathMessage = null
        e.isCancelled = true
        arena.onDeath(player)
    }

    @EventHandler
    fun on(e: EntityDeathEvent) {
        val entity = e.entity
        val arena = getArena(entity) ?: return
        e.droppedExp = 0
        e.drops.clear()
        arena.onKillEntity(entity)
    }

    @EventHandler
    fun on(e: PlayerInteractEvent) {
        val player = e.player
        val block = e.clickedBlock ?: return
        val arena = player.arena ?: return
        e.isCancelled = true
        if (block.type == Material.CHEST && e.action == Action.RIGHT_CLICK_BLOCK) {
            arena.publicChest.open(player)
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun on(e: PlayerItemDamageEvent) {
        val player = e.player
        if (player.inMobArena) {
            e.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun on(e: EntityDamageByEntityEvent) {
        val victim = e.entity as? Player ?: return
        val attacker = when (e.damager) {
            is Player -> e.damager as Player
            is Projectile -> (e.damager as Projectile).shooter as? Player
            else -> null
        } ?: return
        if (victim.inMobArena || attacker.inMobArena) {
            e.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun on(e: PlayerDropItemEvent) {
        val player = e.player
        if (player.inMobArena) {
            e.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun on(e: ItemSpawnEvent) {
        val location = e.location
        if (getArenaInPlay(location) != null) {
            e.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun on(e: FoodLevelChangeEvent) {
        val player = e.entity as Player
        if (player.inMobArena) e.isCancelled = true
    }

    @EventHandler
    fun on(e: EntityTargetEvent) {
        val entity = e.entity
        val arena = getArena(entity) ?: return
        if (e.target !is Player) e.target = arena.livingPlayers.random().player
    }
}
