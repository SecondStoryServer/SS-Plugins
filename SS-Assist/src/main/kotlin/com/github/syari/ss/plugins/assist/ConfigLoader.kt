package com.github.syari.ss.plugins.assist

import com.github.syari.ss.plugins.assist.Main.Companion.plugin
import com.github.syari.ss.plugins.core.code.IConfigLoader
import com.github.syari.ss.plugins.core.config.CreateConfig.config
import com.github.syari.ss.plugins.core.config.type.ConfigDataType
import org.bukkit.command.CommandSender

object ConfigLoader : IConfigLoader {
    override fun load(sender: CommandSender) {
        plugin.config(sender, "config.yml") {
            SpawnOverride.location = get("spawn-override", ConfigDataType.LOCATION, false)
        }
    }
}
