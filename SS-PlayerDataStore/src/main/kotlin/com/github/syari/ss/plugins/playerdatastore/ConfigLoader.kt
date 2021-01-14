package com.github.syari.ss.plugins.playerdatastore

import com.github.syari.ss.plugins.core.Main.Companion.console
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.config.CreateConfig.config
import com.github.syari.ss.plugins.playerdatastore.Main.Companion.plugin
import org.bukkit.command.CommandSender

object ConfigLoader : OnEnable {
    override fun onEnable() {
        load(console)
    }

    lateinit var saveInventoryMode: SaveMode
    lateinit var saveLocationMode: SaveMode

    private fun load(sender: CommandSender) {
        plugin.config(sender, "config.yml") {
            saveInventoryMode = get("inventory", SaveMode.ConfigDataType, SaveMode.Disable)
            saveLocationMode = get("location", SaveMode.ConfigDataType, SaveMode.Disable)
        }
    }
}
