package com.github.syari.ss.plugins.mobarena.arena

import com.github.syari.spigot.api.event.events
import com.github.syari.spigot.api.message.sendActionMessage
import com.github.syari.spigot.api.scheduler.runTaskTimer
import com.github.syari.spigot.api.string.toColor
import com.github.syari.spigot.api.string.toUncolor
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.item.itemStack
import com.github.syari.ss.plugins.mobarena.Main.Companion.plugin
import com.github.syari.ss.plugins.mobarena.MobArenaManager.arena
import com.github.syari.ss.plugins.mobarena.MobArenaManager.arenaPlayer
import com.github.syari.ss.plugins.mobarena.MobArenaManager.getArena
import com.github.syari.ss.plugins.mobarena.MobArenaManager.getArenaInPlay
import com.github.syari.ss.plugins.mobarena.MobArenaManager.inMobArena
import com.github.syari.ss.plugins.mobarena.kit.MobArenaKit
import com.github.syari.ss.plugins.mobarena.shop.Shop
import org.bukkit.Material
import org.bukkit.block.Sign
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.block.Action
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.event.entity.ItemSpawnEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.CraftingInventory

object ArenaEventListener : OnEnable {
    override fun onEnable() {
        plugin.events {
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
            event<EntityDamageByEntityEvent>(ignoreCancelled = true) {
                val victim = it.entity
                if (victim == it.damager) return@event
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
            cancelEventIf<ItemSpawnEvent> {
                getArenaInPlay(it.location) != null
            }
            event<EntityTargetEvent> {
                val entity = it.entity
                val arena = getArena(entity) ?: return@event
                if (it.target !is Player) it.target = arena.livingPlayers.random().player
            }
            val shopLine = "[Shop]"
            val kitLine = "[Kit]"
            val readyLine = "[Ready]"
            event<SignChangeEvent> {
                val player = it.player
                val lines = it.lines
                when (lines[0].toColor().toUncolor()) {
                    shopLine, kitLine, readyLine -> {
                        if (player.isOp) {
                            it.lines.forEachIndexed { index, line ->
                                it.setLine(index, line.toColor())
                            }
                        } else {
                            it.isCancelled = true
                        }
                    }
                }
            }
            event<PlayerInteractEvent> {
                val sign = it.clickedBlock?.state as? Sign ?: return@event
                val lines = sign.lines.map(String::toUncolor)
                val player = it.player
                when (lines[0]) {
                    shopLine -> Shop.get(lines[1])?.openShop(player)
                    kitLine -> {
                        val arenaPlayer = player.arenaPlayer
                        arenaPlayer?.openKitList() ?: MobArenaKit.openPreviewList(player)
                    }
                    readyLine -> {
                        val arenaPlayer = player.arenaPlayer
                        if (arenaPlayer != null && arenaPlayer.play) {
                            if (player.isSneaking) {
                                if (arenaPlayer.ready.not()) return@event player.sendActionMessage("&c&lまだ準備完了していません")
                                arenaPlayer.ready = false
                            } else {
                                if (arenaPlayer.kit == null) return@event player.sendActionMessage("&c&lキットを選択していません")
                                if (arenaPlayer.ready) return@event player.sendActionMessage("&c&l既に準備完了しています")
                                arenaPlayer.ready = true
                            }
                        }
                    }
                }
            }
            val exitItem = itemStack(Material.BARRIER, "&cアリーナを抜ける")
            plugin.runTaskTimer(30) {
                plugin.server.onlinePlayers.forEach { player ->
                    if (player.inMobArena.not()) return@forEach
                    val inventory = player.openInventory.topInventory as? CraftingInventory ?: return@forEach
                    if (inventory.result == null) {
                        inventory.result = exitItem
                        player.updateInventory()
                    }
                }
            }
            event<InventoryClickEvent> {
                val player = it.whoClicked as? Player ?: return@event
                if (player.inMobArena.not()) return@event
                val currentItem = it.currentItem ?: return@event
                if (currentItem.isSimilar(exitItem)) {
                    it.isCancelled = true
                    val arena = player.arena ?: return@event
                    arena.leave(player)
                }
            }
        }
    }
}
