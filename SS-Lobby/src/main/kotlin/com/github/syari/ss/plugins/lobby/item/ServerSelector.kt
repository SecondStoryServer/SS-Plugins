package com.github.syari.ss.plugins.lobby.item

import com.github.syari.ss.plugins.core.inventory.CreateInventory.inventory
import com.github.syari.ss.plugins.core.item.itemStack
import com.github.syari.ss.plugins.core.pluginMessage.PluginMessage
import org.bukkit.Material
import org.bukkit.entity.Player

object ServerSelector : ClickableLobbyItem {
    override val item = itemStack(Material.COMPASS, "&6サーバー選択")

    var servers = mapOf<Int, Server>()

    data class Server(val name: String, val material: Material, val displayName: String, val description: List<String>, val hidden: Boolean)

    override fun onClick(player: Player) {
        inventory("&9&lサーバー選択", 1) {
            servers.forEach { (index, server) ->
                if (server.hidden.not() || player.isOp) {
                    item(index, server.material, server.displayName, server.description).event {
                        if (server.name.isNotBlank()) {
                            PluginMessage.sendBungee(player) {
                                writeUTF("Connect")
                                writeUTF(server.name)
                            }
                        }
                    }
                }
            }
        }.open(player)
    }
}
