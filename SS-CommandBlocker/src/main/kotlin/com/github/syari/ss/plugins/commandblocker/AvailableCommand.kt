package com.github.syari.ss.plugins.commandblocker

import com.github.syari.ss.plugins.core.player.UUIDPlayer
import org.bukkit.entity.Player

object AvailableCommand {
    private val availableCommands = mutableMapOf<UUIDPlayer, List<String>>()

    fun Player.isAvailableCommand(label: String): Boolean {
        val commands = availableCommands[UUIDPlayer(this)] ?: return false
        return commands.contains(label)
    }
}