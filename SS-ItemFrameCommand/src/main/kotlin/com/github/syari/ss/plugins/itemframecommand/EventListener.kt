package com.github.syari.ss.plugins.itemframecommand

import com.github.syari.ss.plugins.core.code.EventRegister
import com.github.syari.ss.plugins.core.code.ListenerFunctions
import com.github.syari.ss.plugins.core.command.RunCommand.runCommand
import com.github.syari.ss.plugins.itemframecommand.ItemFrameCommand.frameCommands
import com.github.syari.ss.plugins.itemframecommand.ItemFrameCommand.isFrameCommandsItem
import org.bukkit.entity.ItemFrame
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.entity.ItemSpawnEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEntityEvent

object EventListener : EventRegister {
    override fun ListenerFunctions.events() {
        event<PlayerInteractEntityEvent> { e ->
            val player = e.player
            if (player.isOp && player.isSneaking) return@event
            val frame = e.rightClicked as? ItemFrame ?: return@event
            frame.item.frameCommands.forEach {
                runCommand(player, it)
            }
        }
        event<EntityDamageEvent>(ignoreCancelled = true) { e ->
            val frame = e.entity as? ItemFrame ?: return@event
            if (frame.item.isFrameCommandsItem) {
                if (e is EntityDamageByEntityEvent) {
                    val player = e.damager as? Player
                    if (player != null && player.isOp && player.isSneaking) return@event
                }
                e.isCancelled = true
            }
        }
        event<EntityPickupItemEvent>(ignoreCancelled = true) { e ->
            if (e.item.itemStack.isFrameCommandsItem) {
                val player = e.entity as? Player
                if (player != null && player.isOp) return@event
                e.isCancelled = true
            }
        }
        event<PlayerDropItemEvent>(ignoreCancelled = true) { e ->
            if (e.itemDrop.itemStack.isFrameCommandsItem) {
                e.isCancelled = true
            }
        }
        event<ItemSpawnEvent>(ignoreCancelled = true) { e ->
            if (e.entity.itemStack.isFrameCommandsItem) {
                e.isCancelled = true
            }
        }
    }
}
