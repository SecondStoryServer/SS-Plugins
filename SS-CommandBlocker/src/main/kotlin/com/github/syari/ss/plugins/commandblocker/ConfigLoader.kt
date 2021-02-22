package com.github.syari.ss.plugins.commandblocker

import com.github.syari.ss.plugins.commandblocker.Main.Companion.plugin
import com.github.syari.ss.plugins.core.code.IConfigLoader
import com.github.syari.ss.plugins.core.config.CreateConfig.config
import com.github.syari.ss.plugins.core.config.type.ConfigDataType
import org.bukkit.command.CommandSender

object ConfigLoader : IConfigLoader {
    override fun load(sender: CommandSender) {
        plugin.config(sender, "config.yml") {
            AvailableCommand.globalAvailableCommands = get("available.global", ConfigDataType.STRINGLIST, listOf(), false)
        }
    }
}
