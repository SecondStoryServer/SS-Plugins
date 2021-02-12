package com.github.syari.ss.plugins.demonkill.craft

import com.github.syari.spigot.api.event.register.EventRegister
import com.github.syari.spigot.api.event.register.Events
import com.github.syari.ss.plugins.core.inventory.CreateInventory.isOpenInventory
import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent

object EventListener : EventRegister {
    override fun Events.register() {
        event<PlayerInteractEvent> {
            if (it.item?.type == Material.CRAFTING_TABLE && isOpenInventory(it.player).not()) {
                CraftInterface.openRoot(it.player)
                it.isCancelled = true
            }
        }
    }
}
