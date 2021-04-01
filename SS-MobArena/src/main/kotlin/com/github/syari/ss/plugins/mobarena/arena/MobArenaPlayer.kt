package com.github.syari.ss.plugins.mobarena.arena

import com.github.syari.ss.plugins.core.inventory.CreateInventory.inventory
import com.github.syari.ss.plugins.mobarena.kit.MobArenaKit
import org.bukkit.Material
import org.bukkit.entity.Player

class MobArenaPlayer(val arena: MobArena, val player: Player, var play: Boolean) {
    var ready = false
        set(value) {
            field = value
            if (value) {
                arena.checkReady(player)
            } else {
                arena.announce("&b[MobArena] &a${player.displayName}&fが準備完了を取り消しました")
            }
        }

    var kit: MobArenaKit? = null

    fun loadKit(kit: MobArenaKit) {
        arena.loadKit(this, kit)
    }

    fun openKitList() {
        val kits = MobArenaKit.kits.filter { it.key in arena.kits }.values
        inventory("&9&lキット選択", 6) {
            kits.forEachIndexed { i, kit ->
                item(i, kit.icon.clone()).event {
                    kit.openPreview(
                        player,
                        onClose = {
                            openKitList()
                        },
                        onSelect = {
                            if (arena.availableKit(kit)) {
                                loadKit(kit)
                                player.closeInventory()
                            }
                        }
                    )
                }
            }
            item(45..53, Material.GRAY_STAINED_GLASS_PANE)
            item(49, Material.RED_STAINED_GLASS_PANE, "&c&l閉じる").event {
                player.closeInventory()
            }
        }.open(player)
    }
}
