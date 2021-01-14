package com.github.syari.ss.plugins.itemframecommand

import com.github.syari.ss.plugins.core.command.RunCommand.runCommand
import com.github.syari.ss.plugins.itemframecommand.ItemFrameCommand.frameCommands
import com.github.syari.ss.plugins.itemframecommand.ItemFrameCommand.isFrameCommandsItem
import org.bukkit.entity.ItemFrame
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.entity.ItemSpawnEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEntityEvent

object EventListener : Listener {
    @EventHandler
    fun on(e: PlayerInteractEntityEvent) {
        val player = e.player
        if (player.isOp && player.isSneaking) return
        val frame = e.rightClicked as? ItemFrame ?: return
        frame.item.frameCommands.forEach {
            runCommand(player, it)
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun on(e: EntityDamageEvent) {
        val frame = e.entity as? ItemFrame ?: return
        if (frame.item.isFrameCommandsItem) {
            if (e is EntityDamageByEntityEvent) {
                val player = e.damager as? Player
                if (player != null && player.isOp && player.isSneaking) return
            }
            e.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun on(e: EntityPickupItemEvent) {
        if (e.item.itemStack.isFrameCommandsItem) {
            val player = e.entity as? Player
            if (player != null && player.isOp) return
            e.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun on(e: PlayerDropItemEvent) {
        if (e.itemDrop.itemStack.isFrameCommandsItem) {
            e.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun on(e: ItemSpawnEvent) {
        if (e.entity.itemStack.isFrameCommandsItem) {
            e.isCancelled = true
        }
    }
}
