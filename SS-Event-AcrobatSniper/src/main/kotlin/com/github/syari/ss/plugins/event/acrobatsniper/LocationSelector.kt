package com.github.syari.ss.plugins.event.acrobatsniper

import com.github.syari.ss.plugins.core.inventory.CreateInventory.inventory
import com.github.syari.ss.plugins.core.item.itemStack
import com.github.syari.ss.plugins.core.pluginMessage.PluginMessage
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player

object LocationSelector {
    val item = itemStack(Material.COMPASS, "&aテレポート")

    var lobbyServerName: String? = null
    var spectateLocation: Location? = null
    var practiceLocation: Location? = null

    @Suppress("UnstableApiUsage")
    fun openInventory(player: Player) {
        inventory("&9&lテレポート", 1) {
            item(1, Material.GOLDEN_BOOTS, "&6ロビー").event {
                lobbyServerName?.let {
                    PluginMessage.sendBungee(player) {
                        writeUTF("Connect")
                        writeUTF(it)
                    }
                }
            }
            item(3, Material.LEATHER_BOOTS, "&6スポーン地点").event {
                player.teleport(player.world.spawnLocation)
            }
            item(5, Material.ENDER_EYE, "&6観戦").event {
                spectateLocation?.let(player::teleport)
            }
            item(7, Material.BOW, "&6練習場").event {
                practiceLocation?.let(player::teleport)
            }
        }.open(player)
    }
}
