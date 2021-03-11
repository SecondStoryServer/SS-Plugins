package com.github.syari.ss.plugins.lobby.gadget

import com.github.syari.ss.plugins.core.item.itemStack
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object Elytra : Gadget(Material.ELYTRA, "エリトラ", "lobby.gadget.elytra") {
    private val elytra = itemStack(Material.ELYTRA, "&dエリトラ").apply {
        isUnbreakable = true
    }

    private val fireworkRocket = itemStack(Material.FIREWORK_ROCKET, "&d花火")

    override fun onEnable(player: Player, itemStack: ItemStack) {
        player.inventory.chestplate = elytra
        player.inventory.setItemInOffHand(fireworkRocket)
        super.onEnable(player, itemStack)
    }

    override fun onDisable(player: Player, itemStack: ItemStack) {
        player.inventory.chestplate = null
        player.inventory.setItemInOffHand(null)
        super.onDisable(player, itemStack)
    }
}
