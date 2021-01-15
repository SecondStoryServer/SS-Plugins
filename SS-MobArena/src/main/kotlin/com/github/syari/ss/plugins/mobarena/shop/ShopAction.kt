package com.github.syari.ss.plugins.mobarena.shop

import org.bukkit.entity.Player

sealed class ShopAction(val target: ShopElement) {
    fun buy(player: Player): Boolean = target.give(player)

    class Free(target: ShopElement) : ShopAction(target)
    class Paid(
        target: ShopElement,
        val needs: List<ShopElement>
    ) : ShopAction(target)

    companion object {
        fun from(list: List<ShopElement>?): ShopAction? {
            return when (list?.size) {
                null, 0 -> null
                1 -> Free(list.first())
                else -> Paid(list.first(), list.drop(1))
            }
        }
    }
}
