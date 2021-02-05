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
        event<EntityDamageEvent>(ignoreCancelled = true) {
            val frame = it.entity as? ItemFrame ?: return@event
            if (frame.item.isFrameCommandsItem) {
                if (it is EntityDamageByEntityEvent) {
                    val player = it.damager as? Player
                    if (player != null && player.isOp && player.isSneaking) return@event
                }
                it.isCancelled = true
            }
        }
        event<EntityPickupItemEvent>(ignoreCancelled = true) {
            if (it.item.itemStack.isFrameCommandsItem) {
                val player = it.entity as? Player
                if (player != null && player.isOp) return@event
                it.isCancelled = true
            }
        }
        cancelEvent<PlayerDropItemEvent> {
            it.itemDrop.itemStack.isFrameCommandsItem
        }
        cancelEvent<ItemSpawnEvent> {
            it.entity.itemStack.isFrameCommandsItem
        }
    }
}
