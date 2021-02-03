package com.github.syari.ss.plugins.playerdatastore

import com.github.syari.ss.plugins.core.config.type.ConfigDataType
import com.github.syari.ss.plugins.core.config.type.data.ConfigItemConverter
import com.github.syari.ss.plugins.core.item.Base64Item
import com.github.syari.ss.plugins.core.scheduler.CreateScheduler.runLater
import com.github.syari.ss.plugins.playerdatastore.Main.Companion.plugin
import org.bukkit.Material

class InventoryData(playerData: PlayerData) : LoadableDataType(playerData) {
    private val inventory
        get() = playerData.config.get("inventory", ConfigDataType.INVENTORY(ConfigItemConverter.Base64), false)

    override val isEnable
        get() = playerData.uuidPlayer.let(ConfigLoader.saveInventoryMode.condition)

    override fun load() {
        if (isLoaded.not()) {
            isLoaded = true
            plugin.runLater(1) {
                val player = playerData.uuidPlayer.player ?: return@runLater
                inventory?.forEach { (slot, item) ->
                    player.inventory.setItem(slot, item)
                }
            }
        }
    }

    override fun save() {
        if (isEnable) {
            val player = playerData.uuidPlayer.player ?: return
            player.inventory.contents.forEachIndexed { slot, item ->
                if (item != null && item.type != Material.AIR) {
                    playerData.config.setUnsafe("inventory.$slot", Base64Item.toBase64(item))
                } else {
                    playerData.config.setUnsafe("inventory.$slot", null)
                }
            }
        } else {
            playerData.config.setUnsafe("inventory", null)
        }
    }
}
