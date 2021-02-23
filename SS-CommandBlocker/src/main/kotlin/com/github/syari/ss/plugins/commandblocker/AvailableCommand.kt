package com.github.syari.ss.plugins.commandblocker

import com.github.syari.spigot.api.util.uuid.UUIDPlayer
import org.bukkit.entity.Player

object AvailableCommand {
    private val list = mutableMapOf<UUIDPlayer, List<String>>()

    var globalAvailableCommands = listOf<String>()

    val Player.availableCommands
        get() = list[UUIDPlayer.from(this)].orEmpty() + globalAvailableCommands
}
