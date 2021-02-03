package com.github.syari.ss.plugins.playerdatastore

import com.github.syari.ss.plugins.core.config.type.ConfigDataType
import org.bukkit.Location

class LocationData(playerData: PlayerData) : DataType(playerData) {
    private val location
        get() = playerData.config.get("location", ConfigDataType.LOCATION, false)

    override val isEnable: Boolean
        get() = playerData.uuidPlayer.let(ConfigLoader.saveLocationMode.condition)

    fun get(): Location? {
        return if (isEnable && isLoaded.not()) {
            isLoaded = true
            location
        } else {
            null
        }
    }

    override fun save() {
        val player = playerData.uuidPlayer.player ?: return
        if (isEnable) {
            playerData.config.set("location", ConfigDataType.LOCATION, player.location)
        } else {
            playerData.config.setUnsafe("location", null)
        }
    }
}
