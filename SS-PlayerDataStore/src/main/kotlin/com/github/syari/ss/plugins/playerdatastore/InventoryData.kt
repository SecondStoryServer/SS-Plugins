package com.github.syari.ss.plugins.playerdatastore

import com.github.syari.spigot.api.config.type.ConfigDataType
import com.github.syari.spigot.api.scheduler.runTaskLater
import com.github.syari.ss.plugins.core.config.ConfigItemConverter
import com.github.syari.ss.plugins.core.config.Inventory
import com.github.syari.ss.plugins.core.item.Base64Item
import com.github.syari.ss.plugins.playerdatastore.Main.Companion.plugin
import org.bukkit.Material

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
