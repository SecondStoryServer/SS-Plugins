package com.github.syari.ss.plugins.playerdatastore

import com.github.syari.spigot.api.config.config
import com.github.syari.spigot.api.config.type.ConfigDataType
import com.github.syari.spigot.api.scheduler.runTaskTimer
import com.github.syari.ss.plugins.core.code.IConfigLoader
import com.github.syari.ss.plugins.playerdatastore.Main.Companion.plugin
import com.github.syari.ss.plugins.playerdatastore.PlayerData.Companion.storeData
import org.bukkit.command.CommandSender

object ConfigLoader : IConfigLoader {
    lateinit var saveInventoryMode: SaveMode
    lateinit var saveLocationMode: SaveMode

    override fun load(sender: CommandSender) {
        plugin.config(sender, "config.yml") {
            val savePeriodTick = get("period", ConfigDataType.Long, 3 * 60)
            saveInventoryMode = get("inventory", SaveMode.ConfigDataType, SaveMode.Disable)
            saveLocationMode = get("location", SaveMode.ConfigDataType, SaveMode.Disable)
            plugin.runTaskTimer(savePeriodTick) {
                plugin.server.onlinePlayers.forEach {
                    it.storeData.saveAll()
                }
            }
        }
    }
}
