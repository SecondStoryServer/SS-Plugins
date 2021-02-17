package com.github.syari.ss.plugins.event.acrobatsniper

import com.github.syari.spigot.api.event.register.EventRegister
import com.github.syari.spigot.api.event.register.Events
import com.github.syari.ss.plugins.core.item.CustomItemStack
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

object EventListener : EventRegister {
    override fun Events.register() {
        event<PlayerJoinEvent> {
            it.player.inventory.addItem(
                CustomItemStack.create(Material.BOW, "&d弓").apply {
                    addEnchant(Enchantment.ARROW_DAMAGE, 5)
                    addEnchant(Enchantment.ARROW_INFINITE, 1)
                    unbreakable = true
                    addItemFlag(ItemFlag.HIDE_UNBREAKABLE)
                }.toOneItemStack,
                ItemStack(Material.ARROW)
            )
        }
    }
}
