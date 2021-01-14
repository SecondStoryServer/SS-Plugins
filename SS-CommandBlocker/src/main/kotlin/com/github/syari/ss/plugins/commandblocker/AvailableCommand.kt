package com.github.syari.ss.plugins.commandblocker

import com.github.syari.ss.plugins.core.player.UUIDPlayer
import org.bukkit.entity.Player

object AvailableCommand {
    private val list = mutableMapOf<UUIDPlayer, List<String>>()

    val Player.availableCommands
        get() = list[UUIDPlayer(this)].orEmpty()
}
