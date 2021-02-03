package com.github.syari.ss.plugins.playerdatastore

import com.github.syari.ss.plugins.core.Main.Companion.console
import com.github.syari.ss.plugins.core.code.IConfigLoader
import com.github.syari.ss.plugins.core.config.CreateConfig.config
import com.github.syari.ss.plugins.core.config.type.ConfigDataType
import com.github.syari.ss.plugins.core.scheduler.CreateScheduler.runTimer
import com.github.syari.ss.plugins.playerdatastore.Main.Companion.plugin
import com.github.syari.ss.plugins.playerdatastore.PlayerData.Companion.storeData
import org.bukkit.command.CommandSender

object ConfigLoader : IConfigLoader {
    override fun onEnable() {
        load(console)
    }

    lateinit var saveInventoryMode: SaveMode
    lateinit var saveLocationMode: SaveMode

    override fun load(sender: CommandSender) {
        plugin.config(sender, "config.yml") {
            val savePeriodTick = get("period", ConfigDataType.LONG, 3 * 60)
            saveInventoryMode = get("inventory", SaveMode.ConfigDataType, SaveMode.Disable)
            saveLocationMode = get("location", SaveMode.ConfigDataType, SaveMode.Disable)
            plugin.runTimer(savePeriodTick) {
                plugin.server.onlinePlayers.forEach {
                    it.storeData.saveAll()
                }
            }
        }
    }
}
