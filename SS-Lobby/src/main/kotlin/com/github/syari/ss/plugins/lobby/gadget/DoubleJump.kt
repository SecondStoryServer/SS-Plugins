package com.github.syari.ss.plugins.lobby.gadget

import com.github.syari.ss.plugins.core.item.CustomItemStack
import org.bukkit.Material
import org.bukkit.entity.Player

object DoubleJump : Gadget(Material.LEATHER_BOOTS, "ダブルジャンプ", "lobby.gadget.doublejump") {
    private val leatherBoots = CustomItemStack.create(
        Material.LEATHER_BOOTS,
        "&dダブルジャンプの靴"
    ).toOneItemStack

    override fun onEnable(player: Player, itemStack: CustomItemStack) {
        player.inventory.boots = leatherBoots
        super.onEnable(player, itemStack)
    }

    override fun onDisable(player: Player, itemStack: CustomItemStack) {
        player.inventory.boots = null
        super.onDisable(player, itemStack)
    }
}
