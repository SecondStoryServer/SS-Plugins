package com.github.syari.ss.plugins.mobarena.lobby

import com.github.syari.spigot.api.config.config
import com.github.syari.spigot.api.config.type.ConfigDataType
import com.github.syari.spigot.api.config.type.ConfigSectionType
import com.github.syari.ss.plugins.core.inventory.CreateInventory.inventory
import com.github.syari.ss.plugins.core.item.itemStack
import com.github.syari.ss.plugins.core.pluginMessage.PluginMessage
import com.github.syari.ss.plugins.mobarena.Main.Companion.plugin
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object ServerSelector : LobbyItem {
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

    @OptIn(ExperimentalStdlibApi::class)
    fun load(sender: CommandSender) {
        plugin.config(sender, "servers.yml") {
            servers = buildMap {
                section("server", ConfigSectionType.Int)?.forEach {
                    val name = get("server.$it.name", ConfigDataType.String, "")
                    val material = get("server.$it.material", ConfigDataType.Material) ?: return@forEach
                    val display = get("server.$it.display", ConfigDataType.String) ?: return@forEach
                    val description = get("server.$it.description", ConfigDataType.StringList) ?: return@forEach
                    val hidden = get("server.$it.hidden", ConfigDataType.Boolean, false, notFoundError = false)
                    put(it, Server(name, material, display, description, hidden))
                }
            }
        }
    }
}
