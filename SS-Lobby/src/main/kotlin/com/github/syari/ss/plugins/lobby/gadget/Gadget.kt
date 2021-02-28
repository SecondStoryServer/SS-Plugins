package com.github.syari.ss.plugins.lobby.gadget

import com.github.syari.ss.plugins.core.item.itemStack
import com.github.syari.ss.plugins.core.message.Message.action
import com.github.syari.ss.plugins.lobby.LobbyItem
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

open class Gadget(
    material: Material,
    name: String,
    private val permission: String?
) : LobbyItem {
    override val item = itemStack(material, "&d[Gadget] &6$name", "&b手に持ってクリックで切り替え").apply {
        addItemFlags(ItemFlag.HIDE_ENCHANTS)
    }

    open fun onEnable(player: Player, itemStack: ItemStack) {
        itemStack.addEnchant(Enchantment.DURABILITY, 0, true)
    }

    open fun onDisable(player: Player, itemStack: ItemStack) {
        itemStack.removeEnchant(Enchantment.DURABILITY)
    }

    fun toggle(player: Player, itemStack: ItemStack) {
        if (permission?.let { player.hasPermission(it) } != false) {
            if (itemStack.hasEnchant(Enchantment.DURABILITY)) {
                onDisable(player, itemStack)
            } else {
                onEnable(player, itemStack)
            }
        } else {
            player.action("&c&l必要な権限がありません")
        }
    }
}
