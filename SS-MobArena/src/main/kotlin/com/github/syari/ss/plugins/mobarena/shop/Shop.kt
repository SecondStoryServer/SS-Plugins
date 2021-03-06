package com.github.syari.ss.plugins.mobarena.shop

import com.github.syari.spigot.api.inventory.inventory
import com.github.syari.spigot.api.item.editLore
import org.bukkit.entity.Player

data class Shop(
    val name: String,
    val line: Int,
    val list: Map<Int, ShopAction>
) {
    companion object {
        var list = mapOf<String, Shop>()

        fun get(id: String) = list[id]

        val names
            get() = list.keys
    }

    fun openShop(player: Player) {
        inventory(name, line) {
            list.forEach { (index, action) ->
                var canBuy = true
                val item = action.target.display.clone().apply {
                    editLore {
                        if (isNotEmpty()) add("")
                        if (action is ShopAction.Paid) {
                            addAll(
                                action.needs.map {
                                    if (it.has(player)) {
                                        "&a✔"
                                    } else {
                                        canBuy = false
                                        "&c✖"
                                    } + " &r" + it.needsText
                                }
                            )
                            add("")
                        }
                        add((if (canBuy) "&a" else "&c") + action.target.targetText)
                    }
                }
                item(index, item) {
                    onClick {
                        var isReopen = true
                        if (canBuy) {
                            isReopen = action.buy(player)
                            (action as? ShopAction.Paid)?.needs?.forEach {
                                it.remove(player)
                            }
                        }
                        if (isReopen) openShop(player)
                    }
                }
            }
        }.open(player)
    }
}
