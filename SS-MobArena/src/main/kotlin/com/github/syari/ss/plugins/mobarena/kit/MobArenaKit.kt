package com.github.syari.ss.plugins.mobarena.kit

import com.github.syari.spigot.api.item.editLore
import com.github.syari.spigot.api.string.toUncolor
import com.github.syari.ss.plugins.core.inventory.CreateInventory.inventory
import com.github.syari.ss.plugins.mobarena.MobArenaManager
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import com.github.syari.spigot.api.item.displayName as eDisplayName

class MobArenaKit(
    val id: String,
    val name: String,
    icon: ItemStack,
    description: List<String>,
    difficulty: Int,
    val items: Map<Int, ItemStack>
) {
    companion object {
        var kits = mapOf<String, MobArenaKit>()
            set(value) {
                field = value
                kitPage = kits.values.toList().chunked(45)
            }

        private lateinit var kitPage: List<List<MobArenaKit>>

        fun getKit(id: String?) = kits[id]

        fun openPreviewList(player: Player, page: Int = 0) {
            val lastPage = kitPage.lastIndex
            when {
                page < 0 -> openPreviewList(player, 0)
                lastPage < page -> openPreviewList(player, lastPage)
                else -> inventory("&9&lキット一覧 ($page/$lastPage)", 6) {
                    kitPage[page].forEachIndexed { i, kit ->
                        item(
                            i,
                            kit.icon.clone().apply {
                                editLore {
                                    add("")
                                    add("&a使用アリーナ:")
                                    addAll(MobArenaManager.arenas.filter { it.kits.contains(kit.id) }.map { " &7- ${it.id}" })
                                }
                            }
                        ).event {
                            kit.openPreview(player) {
                                openPreviewList(player, page)
                            }
                        }
                    }
                    item(45..53, Material.GRAY_STAINED_GLASS_PANE)
                    item(47, Material.ORANGE_STAINED_GLASS_PANE, "&d<<").event {
                        openPreviewList(player, page - 1)
                    }
                    item(51, Material.ORANGE_STAINED_GLASS_PANE, "&d>>").event {
                        openPreviewList(player, page + 1)
                    }
                    item(49, Material.RED_STAINED_GLASS_PANE, "&c&l閉じる").event {
                        player.closeInventory()
                    }
                }.open(player)
            }
        }
    }

    fun load(player: Player) {
        player.inventory.clear()
        player.setItemOnCursor(null)
        items.forEach { (slot, item) ->
            player.inventory.setItem(slot, item)
        }
    }

    val icon = icon.clone().apply {
        eDisplayName = "&b$name"
        editLore {
            addAll(description)
            add("")
            add("&7難易度: &6" + "⭐".repeat(difficulty))
        }
    }

    fun openPreview(player: Player, onClose: () -> Unit) {
        inventory("&9&l${name.toUncolor()}", 6) {
            var slot = 0
            for (i in 9 until 36) {
                items[i]?.let { item(slot, it) }
                slot ++
            }
            for (i in 0 until 9) {
                items[i]?.let { item(slot, it) }
                slot ++
            }
            slot += 2
            for (i in 40 downTo 36) {
                items[i]?.let { item(slot, it) }
                slot ++
            }
            item((36..37) + (43..48) + (50..53), Material.GRAY_STAINED_GLASS_PANE)
            item(49, Material.RED_STAINED_GLASS_PANE, "&c&l閉じる").event(onClose)
        }.open(player)
    }
}
