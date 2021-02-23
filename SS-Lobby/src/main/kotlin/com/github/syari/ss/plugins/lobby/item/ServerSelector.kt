package com.github.syari.ss.plugins.lobby.item

import com.github.syari.ss.plugins.core.inventory.CreateInventory.inventory
import com.github.syari.ss.plugins.core.item.CustomItemStack
import com.github.syari.ss.plugins.core.pluginMessage.PluginMessage
import org.bukkit.Material
import org.bukkit.entity.Player

object ServerSelector : ClickableLobbyItem {
    override val item = CustomItemStack.create(
        Material.COMPASS,
        "&6サーバー選択"
    ).toOneItemStack

    var servers = mapOf<Int, Server>()

    data class Server(val name: String, val material: Material, val displayName: String, val description: List<String>)

    override fun onClick(player: Player) {
        inventory("&9&lサーバー選択", 1) {
            servers.forEach { (index, server) ->
                item(index, server.material, server.displayName, server.description).event {
                    PluginMessage.sendBungee(player) {
                        writeUTF("Connect")
                        writeUTF(server.name)
                    }
                }
            }
        }.open(player)
    }
}
