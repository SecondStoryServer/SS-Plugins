package com.github.syari.ss.plugins.lobby

import com.github.syari.spigot.api.config.config
import com.github.syari.spigot.api.config.type.ConfigDataType
import com.github.syari.spigot.api.config.type.ConfigSectionType
import com.github.syari.ss.plugins.core.code.IConfigLoader
import com.github.syari.ss.plugins.lobby.Main.Companion.plugin
import com.github.syari.ss.plugins.lobby.item.ServerSelector
import org.bukkit.command.CommandSender

object ConfigLoader : IConfigLoader {
    @OptIn(ExperimentalStdlibApi::class)
    override fun load(sender: CommandSender) {
        plugin.config(sender, "config.yml") {
            ServerSelector.servers = buildMap {
                section("server", ConfigSectionType.Int)?.forEach {
                    val name = get("server.$it.name", ConfigDataType.String) ?: return@forEach
                    val material = get("server.$it.material", ConfigDataType.Material) ?: return@forEach
                    val display = get("server.$it.display", ConfigDataType.String) ?: return@forEach
                    val description = get("server.$it.description", ConfigDataType.StringList) ?: return@forEach
                    put(it, ServerSelector.Server(name, material, display, description))
                }
            }
        }
    }
}
