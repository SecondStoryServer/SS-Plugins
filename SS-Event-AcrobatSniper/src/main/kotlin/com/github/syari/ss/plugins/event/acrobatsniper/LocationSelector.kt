package com.github.syari.ss.plugins.event.acrobatsniper

import com.github.syari.ss.plugins.core.inventory.CreateInventory.inventory
import com.github.syari.ss.plugins.core.item.CustomItemStack
import com.github.syari.ss.plugins.event.acrobatsniper.Main.Companion.plugin
import com.google.common.io.ByteStreams
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player

object LocationSelector {
    val item = CustomItemStack.create(Material.COMPASS, "&aテレポート")

    var lobbyServerName: String? = null
    var spectateLocation: Location? = null
    var practiceLocation: Location? = null

    @Suppress("UnstableApiUsage")
    fun openInventory(player: Player) {
        inventory("&9&lテレポート", 1) {
            item(1, Material.GOLDEN_BOOTS, "&6ロビー").event {
                lobbyServerName?.let {
                    val dataOutput = ByteStreams.newDataOutput()
                    dataOutput.writeUTF("Connect")
                    dataOutput.writeUTF(it)
                    player.sendPluginMessage(plugin, "BungeeCord", dataOutput.toByteArray())
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
