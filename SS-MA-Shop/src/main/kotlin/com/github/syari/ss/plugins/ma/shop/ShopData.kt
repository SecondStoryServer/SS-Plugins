package com.github.syari.ss.plugins.ma.shop

import com.github.syari.ss.plugins.core.inventory.CreateInventory.inventory
import org.bukkit.entity.Player

data class ShopData(
    val name: String,
    val line: Int,
    val list: Map<Int, ShopBuyAction>
) {
    fun open(player: Player) {
        inventory(name, line) {
            list.forEach { (index, action) ->
                var canBuy = true
                val item = action.target.display.clone {
                    display = "&b" + (display?.ifEmpty { null } ?: i18NDisplayName) + " × " + amount
                    editLore {
                        if (action is ShopBuyAction.Paid) {
                            addAll(action.needs.map {
                                if (it.has(player)) {
                                    "&a"
                                } else {
                                    canBuy = false
                                    "&c"
                                } + it.needsText
                            })
                            add("")
                        }
                        add((if (canBuy) "&a" else "&c") + action.target.targetText)
                    }
                }
                item(index, item).event {
                    var isReopen = true
                    if (canBuy) {
                        isReopen = action.buy(player)
                        (action as? ShopBuyAction.Paid)?.needs?.forEach {
                            it.remove(player)
                        }
                    }
                    if (isReopen) this@ShopData.open(player)
                }
            }
        }.open(player)
    }
}
