package com.github.syari.ss.plugins.assist

import com.github.syari.spigot.api.config.config
import com.github.syari.spigot.api.config.type.ConfigDataType
import com.github.syari.ss.plugins.assist.Main.Companion.plugin
import com.github.syari.ss.plugins.core.code.IConfigLoader
import org.bukkit.command.CommandSender

object ConfigLoader : IConfigLoader {
    override fun load(sender: CommandSender) {
        plugin.config(sender, "config.yml") {
            SpawnOverride.location = get("spawn-override", ConfigDataType.Location, false)
        }
    }
}
