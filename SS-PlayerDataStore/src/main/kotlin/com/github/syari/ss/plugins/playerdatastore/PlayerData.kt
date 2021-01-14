package com.github.syari.ss.plugins.playerdatastore

import com.github.syari.ss.plugins.core.Main.Companion.console
import com.github.syari.ss.plugins.core.config.CreateConfig.config
import com.github.syari.ss.plugins.core.config.dataType.ConfigDataType
import com.github.syari.ss.plugins.core.config.dataType.ConfigItemConverter
import com.github.syari.ss.plugins.core.item.Base64Item
import com.github.syari.ss.plugins.playerdatastore.Main.Companion.plugin
import org.bukkit.Material
import org.bukkit.entity.Player

class PlayerData(private val player: Player) {
    companion object {
        fun loadStoreData(player: Player) {
            PlayerData(player).load()
        }

        fun saveStoreData(player: Player) {
            PlayerData(player).save()
        }
    }

    private val config = plugin.config(console, "data/${player.uniqueId}.yml")

    @OptIn(ExperimentalStdlibApi::class)
    private val inventory
        get() = config.get("inventory", ConfigDataType.INVENTORY(ConfigItemConverter.Base64), false)

    private val location
        get() = config.get("location", ConfigDataType.LOCATION, false)

    private val isEnableInventory = player.let(ConfigLoader.saveInventoryMode.condition)

    private val isEnableLocation = player.let(ConfigLoader.saveLocationMode.condition)

    fun load() {
        if (isEnableInventory) {
            inventory?.forEach { (slot, item) ->
                player.inventory.setItem(slot, item)
            }
        }
        if (isEnableLocation) {
            location?.let { player.teleport(it) }
        }
    }

    fun save() {
        if (isEnableInventory) {
            player.inventory.contents.forEachIndexed { slot, item ->
                if (item != null && item.type != Material.AIR) {
                    config.set("inventory.$slot", Base64Item.toBase64(item))
                } else {
                    config.set("inventory.$slot", null)
                }
            }
        } else {
            config.set("inventory", null)
        }
        if (isEnableLocation) {
            config.set("location", ConfigDataType.LOCATION.toString(player.location))
        } else {
            config.set("location", null)
        }
        config.save()
    }
}