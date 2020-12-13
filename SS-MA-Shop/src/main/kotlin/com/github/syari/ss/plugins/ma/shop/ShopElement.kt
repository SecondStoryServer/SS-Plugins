package com.github.syari.ss.plugins.ma.shop

import com.github.syari.ss.plugins.core.item.CustomItemStack
import com.github.syari.ss.plugins.core.item.ItemStackPlus.give
import com.github.syari.ss.plugins.core.item.ItemStackPlus.hasItem
import com.github.syari.ss.plugins.core.item.ItemStackPlus.removeItem
import net.md_5.bungee.api.chat.TranslatableComponent
import org.bukkit.Material
import org.bukkit.entity.Player

sealed class ShopElement {
    open fun give(player: Player) = true
    open fun has(player: Player) = false
    open fun remove(player: Player) {}
    open val display = CustomItemStack.create(Material.BARRIER, "&cエラー")
    open val targetText = ""
    open val needsText = ""

    class Jump(
        private val id: String,
        private val type: Material
    ): ShopElement() {
        override fun give(player: Player) = Shop.get(id)?.open(player)?.run { false } ?: true

        override val display by lazy { CustomItemStack.create(type, "&6${Shop.get(id)?.name}") }
        override val targetText = "クリックで開く"
    }

    sealed class Item: ShopElement() {
        open val item: CustomItemStack? = null

        override val display by lazy { item ?: CustomItemStack.create(Material.STONE, "&cエラー") }
        override val targetText = "クリックで購入する"

        override fun give(player: Player): Boolean {
            item?.let { player.give(it) }
            return true
        }

        override fun has(player: Player) = item?.let { player.hasItem(it) } ?: false

        override fun remove(player: Player) {
            item?.let { player.removeItem(it) }
        }

        class Minecraft(
            type: Material,
            amount: Int
        ): Item() {
            override val item = CustomItemStack.create(type, amount)
            override val needsText by lazy { "${item.display?.ifEmpty { null } ?: item.i18NDisplayName} × $amount" }
        }
    }

    object UnAvailable: ShopElement()

    companion object {
        fun from(line: String): ShopElement {
            val split = line.split("\\s+".toRegex())
            return when (split[0].toLowerCase()) {
                "jump" -> {
                    val id = split.getOrNull(1)
                    if (id != null) {
                        val type = split.getOrNull(2)?.let { Material.getMaterial(it) } ?: Material.COMPASS
                        Jump(id, type)
                    } else {
                        UnAvailable
                    }
                }
                "mc" -> {
                    val type = split.getOrNull(1)?.let { Material.getMaterial(it) }
                    if (type != null) {
                        val amount = split.getOrNull(2)?.toIntOrNull() ?: 1
                        Item.Minecraft(type, amount)
                    } else {
                        UnAvailable
                    }
                }
                else -> UnAvailable
            }
        }
    }
}
