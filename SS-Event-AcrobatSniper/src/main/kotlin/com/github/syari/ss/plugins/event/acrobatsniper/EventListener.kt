package com.github.syari.ss.plugins.event.acrobatsniper

import com.github.syari.spigot.api.event.events
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.item.itemStack
import com.github.syari.ss.plugins.event.acrobatsniper.Main.Companion.plugin
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

object EventListener : OnEnable {
    override fun onEnable() {
        val bow = itemStack(Material.BOW, "&d弓").apply {
            addEnchant(Enchantment.ARROW_DAMAGE, 5, true)
            addEnchant(Enchantment.ARROW_INFINITE, 1, true)
            isUnbreakable = true
            addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
        }
        plugin.events {
            event<PlayerJoinEvent> {
                it.player.inventory.apply {
                    setItem(0, bow)
                    setItem(27, ItemStack(Material.ARROW))
                    setItem(8, LocationSelector.item)
                }
            }
            event<PlayerInteractEvent> {
                val item = it.item ?: return@event
                if (LocationSelector.item.isSimilar(item)) {
                    it.isCancelled = true
                    LocationSelector.openInventory(it.player)
                }
            }
        }
    }
}
