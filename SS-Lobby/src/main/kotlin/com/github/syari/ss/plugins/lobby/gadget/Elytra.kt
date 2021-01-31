package com.github.syari.ss.plugins.lobby.gadget

import com.github.syari.ss.plugins.core.item.CustomItemStack
import org.bukkit.Material
import org.bukkit.entity.Player

object Elytra : Gadget(Material.ELYTRA, "エリトラ", "lobby.gadget.elytra") {
    private val elytra = CustomItemStack.create(
        Material.ELYTRA,
        "&dエリトラ"
    ).apply {
        unbreakable = true
    }.toOneItemStack

    private val fireworkRocket = CustomItemStack.create(
        Material.FIREWORK_ROCKET,
        "&d花火"
    ).toOneItemStack

    override fun onEnable(player: Player, itemStack: CustomItemStack) {
        player.inventory.chestplate = elytra
        player.inventory.setItemInOffHand(fireworkRocket)
        super.onEnable(player, itemStack)
    }

    override fun onDisable(player: Player, itemStack: CustomItemStack) {
        player.inventory.chestplate = null
        player.inventory.setItemInOffHand(null)
        super.onDisable(player, itemStack)
    }
}
