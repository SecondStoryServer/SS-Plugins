package com.github.syari.ss.plugins.event.acrobatsniper

import com.github.syari.ss.plugins.core.code.IConfigLoader
import com.github.syari.ss.plugins.core.config.CreateConfig.config
import com.github.syari.ss.plugins.core.config.type.ConfigDataType
import com.github.syari.ss.plugins.event.acrobatsniper.Main.Companion.plugin
import org.bukkit.command.CommandSender

object ConfigLoader : IConfigLoader {
    override fun load(sender: CommandSender) {
        plugin.config(sender, "config.yml") {
            Match.spawnLocation1 = get("match.spawn1", ConfigDataType.LOCATION)
            Match.spawnLocation2 = get("match.spawn2", ConfigDataType.LOCATION)
            MatchPlayer.maxLife = get("match.life", ConfigDataType.INT, 1)
        }
    }
}
