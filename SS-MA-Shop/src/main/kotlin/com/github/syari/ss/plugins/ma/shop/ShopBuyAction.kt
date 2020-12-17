package com.github.syari.ss.plugins.ma.shop

import org.bukkit.entity.Player

sealed class ShopBuyAction(val target: ShopElement) {
    fun buy(player: Player): Boolean = target.give(player)

    class Free(target: ShopElement): ShopBuyAction(target)
    class Paid(
        target: ShopElement, val needs: List<ShopElement>
    ): ShopBuyAction(target)

    companion object {
        fun from(list: List<ShopElement>?): ShopBuyAction? {
            return when (list?.size) {
                null, 0 -> null
                1 -> Free(list.first())
                else -> Paid(list.first(), list.drop(1))
            }
        }
    }
}