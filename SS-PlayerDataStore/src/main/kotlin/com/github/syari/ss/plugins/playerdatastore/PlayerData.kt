package com.github.syari.ss.plugins.playerdatastore

import com.github.syari.ss.plugins.core.Main.Companion.console
import com.github.syari.ss.plugins.core.config.CreateConfig.config
import com.github.syari.ss.plugins.core.config.type.ConfigDataType
import com.github.syari.ss.plugins.core.config.type.data.ConfigItemConverter
import com.github.syari.ss.plugins.core.item.Base64Item
import com.github.syari.ss.plugins.core.player.UUIDPlayer
import com.github.syari.ss.plugins.playerdatastore.Main.Companion.plugin
import org.bukkit.Material
import org.bukkit.entity.Player

class PlayerData(private val uuidPlayer: UUIDPlayer) {
    companion object {
        private val storeDataList = mutableMapOf<UUIDPlayer, PlayerData>()

        private val UUIDPlayer.storeData
            get() = storeDataList.getOrPut(this) { PlayerData(this) }

        private val Player.storeData
            get() = UUIDPlayer(this).storeData

        fun loadStoreData(player: Player) {
            player.storeData.load()
        }

        fun saveStoreData(player: Player) {
            player.storeData.save()
        }
    }

    private val config by lazy { plugin.config(console, "data/$uuidPlayer.yml") }

    @OptIn(ExperimentalStdlibApi::class)
    private val inventory
        get() = config.get("inventory", ConfigDataType.INVENTORY(ConfigItemConverter.Base64), false)

    private val location
        get() = config.get("location", ConfigDataType.LOCATION, false)

    private val isEnableInventory
        get() = uuidPlayer.let(ConfigLoader.saveInventoryMode.condition)

    private val isEnableLocation
        get() = uuidPlayer.let(ConfigLoader.saveLocationMode.condition)

    private val isEnableSave
        get() = isEnableInventory || isEnableLocation

    private var isLoadedInventory = false

    private var isLoadedLocation = false

    fun load() {
        if (isEnableSave.not()) return
        val player = uuidPlayer.player ?: return
        if (isEnableInventory && isLoadedInventory.not()) {
            isLoadedInventory = true
            inventory?.forEach { (slot, item) ->
                player.inventory.setItem(slot, item)
            }
        }
        if (isEnableLocation && isLoadedLocation.not()) {
            isLoadedLocation = true
            location?.let { player.teleport(it) }
        }
    }

    fun save() {
        if (isEnableSave.not()) return
        val player = uuidPlayer.player ?: return
        if (isLoadedInventory) {
            isLoadedInventory = false
            if (isEnableInventory) {
                player.inventory.contents.forEachIndexed { slot, item ->
                    if (item != null && item.type != Material.AIR) {
                        config.setUnsafe("inventory.$slot", Base64Item.toBase64(item))
                    } else {
                        config.setUnsafe("inventory.$slot", null)
                    }
                }
            } else {
                config.setUnsafe("inventory", null)
            }
        }
        if (isLoadedLocation) {
            isLoadedLocation = false
            if (isEnableLocation) {
                config.set("location", ConfigDataType.LOCATION, player.location)
            } else {
                config.setUnsafe("location", null)
            }
        }
        config.save()
    }
}
