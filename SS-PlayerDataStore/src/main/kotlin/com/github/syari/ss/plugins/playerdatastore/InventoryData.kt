package com.github.syari.ss.plugins.playerdatastore

import com.github.syari.spigot.api.config.type.ConfigDataType
import com.github.syari.spigot.api.scheduler.runTaskLater
import com.github.syari.ss.plugins.core.config.ConfigItemConverter
import com.github.syari.ss.plugins.core.config.asInventoryMap
import com.github.syari.ss.plugins.playerdatastore.Main.Companion.plugin

class InventoryData(playerData: PlayerData) : LoadableDataType(playerData) {
    private val inventory
        get() = playerData.config.get("inventory", ConfigDataType.Inventory(ConfigItemConverter.Base64), false)

    override val isEnable
        get() = playerData.uuidPlayer.let(ConfigLoader.saveInventoryMode.condition)

    override fun load() {
        if (isLoaded.not()) {
            isLoaded = true
            plugin.runTaskLater(1) {
                val player = playerData.uuidPlayer.player ?: return@runTaskLater
                inventory?.forEach { (slot, item) ->
                    player.inventory.setItem(slot, item)
                }
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun save() {
        if (isEnable) {
            val player = playerData.uuidPlayer.player ?: return
            val inventory = player.inventory.contents.asInventoryMap()
            playerData.config.set("inventory", ConfigDataType.Inventory(ConfigItemConverter.Base64), inventory)
        } else {
            playerData.config.setNull("inventory")
        }
    }
}
