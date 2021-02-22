package com.github.syari.ss.plugins.lobby

import com.github.syari.ss.plugins.core.code.IConfigLoader
import com.github.syari.ss.plugins.core.config.CreateConfig.config
import com.github.syari.ss.plugins.core.config.type.ConfigDataType
import com.github.syari.ss.plugins.core.config.type.ConfigSectionType
import com.github.syari.ss.plugins.lobby.Main.Companion.plugin
import com.github.syari.ss.plugins.lobby.item.ServerSelector
import org.bukkit.command.CommandSender

object ConfigLoader : IConfigLoader {
    @OptIn(ExperimentalStdlibApi::class)
    override fun load(sender: CommandSender) {
        plugin.config(sender, "config.yml") {
            ServerSelector.servers = buildMap {
                section("server", ConfigSectionType.INT)?.forEach {
                    val name = get("server.$it.name", ConfigDataType.STRING) ?: return@forEach
                    val material = get("server.$it.material", ConfigDataType.MATERIAL) ?: return@forEach
                    val display = get("server.$it.display", ConfigDataType.STRING) ?: return@forEach
                    val description = get("server.$it.description", ConfigDataType.STRINGLIST) ?: return@forEach
                    put(it, ServerSelector.Server(name, material, display, description))
                }
            }
        }
    }
}
