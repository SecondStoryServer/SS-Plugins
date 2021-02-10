package com.github.syari.ss.plugins.playerdatastore

import com.github.syari.spigot.api.util.uuid.UUIDPlayer
import com.github.syari.ss.plugins.core.Main.Companion.console
import com.github.syari.ss.plugins.core.config.CreateConfig.config
import com.github.syari.ss.plugins.playerdatastore.Main.Companion.plugin
import org.bukkit.entity.Player

class PlayerData(val uuidPlayer: UUIDPlayer) {
    companion object {
        private val storeDataList = mutableMapOf<UUIDPlayer, PlayerData>()

        private val UUIDPlayer.storeData
            get() = storeDataList.getOrPut(this) { PlayerData(this) }

        val Player.storeData
            get() = UUIDPlayer.from(this).storeData
    }

    val config by lazy { plugin.config(console, "data/$uuidPlayer.yml") }

    val inventory = InventoryData(this)

    val location = LocationData(this)

    fun saveAll() {
        inventory.save()
        location.save()
        config.save()
    }

    fun unloadAll() {
        inventory.unload()
        location.unload()
        config.save()
    }
}
