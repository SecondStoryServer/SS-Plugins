package com.github.syari.ss.plugins.demonkill.craft

import com.github.syari.spigot.api.event.events
import com.github.syari.spigot.api.inventory.inventoryId
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.demonkill.Main.Companion.plugin
import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent

object EventListener : OnEnable {
    override fun onEnable() {
        plugin.events {
            event<PlayerInteractEvent> {
                if (it.item?.type == Material.CRAFTING_TABLE && it.player.inventoryId == null) {
                    CraftInterface.openRoot(it.player)
                    it.isCancelled = true
                }
            }
        }
    }
}
