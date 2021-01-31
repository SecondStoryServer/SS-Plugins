package com.github.syari.ss.plugins.lobby.gadget

import com.github.syari.ss.plugins.core.item.CustomItemStack
import com.github.syari.ss.plugins.lobby.LobbyItem
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag

open class Gadget(
    material: Material,
    name: String,
    private val permission: String?
) : LobbyItem {
    override val item = CustomItemStack.create(
        material,
        "&d[Gadget] &6$name",
        "&b手に持って左クリックで切り替え"
    ).apply {
        addItemFlag(ItemFlag.HIDE_ENCHANTS)
    }.toOneItemStack

    open fun onEnable(player: Player, itemStack: CustomItemStack) {
        itemStack.addEnchant(Enchantment.DURABILITY, 0)
    }

    open fun onDisable(player: Player, itemStack: CustomItemStack) {
        itemStack.removeEnchant(Enchantment.DURABILITY)
    }

    fun toggle(player: Player, itemStack: CustomItemStack) {
        if (permission?.let { player.hasPermission(it) } != false) {
            if (itemStack.hasEnchant(Enchantment.DURABILITY)) {
                onDisable(player, itemStack)
            } else {
                onEnable(player, itemStack)
            }
        }
    }
}
