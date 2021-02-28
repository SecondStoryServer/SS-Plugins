package com.github.syari.ss.plugins.event.acrobatsniper

import com.github.syari.spigot.api.config.config
import com.github.syari.spigot.api.config.type.ConfigDataType
import com.github.syari.ss.plugins.core.code.IConfigLoader
import com.github.syari.ss.plugins.event.acrobatsniper.Main.Companion.plugin
import org.bukkit.command.CommandSender

object ConfigLoader : IConfigLoader {
    override fun load(sender: CommandSender) {
        plugin.config(sender, "config.yml") {
            Match.spawnLocation1 = get("match.spawn1", ConfigDataType.Location)
            Match.spawnLocation2 = get("match.spawn2", ConfigDataType.Location)
            MatchPlayer.maxLife = get("match.life", ConfigDataType.Int, 1)
            LocationSelector.lobbyServerName = get("selector.lobby", ConfigDataType.String, "lobby")
            LocationSelector.spectateLocation = get("selector.spectate", ConfigDataType.Location)
            LocationSelector.practiceLocation = get("selector.practice", ConfigDataType.Location)
        }
    }
}
