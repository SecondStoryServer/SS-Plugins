package com.github.syari.ss.wplugins.accessblocker

import com.github.syari.ss.wplugins.accessblocker.Main.Companion.plugin
import com.github.syari.ss.wplugins.core.message.JsonBuilder.Companion.buildJson
import com.github.syari.ss.wplugins.core.message.Message.send
import net.md_5.bungee.api.event.ServerConnectedEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

object ModBlocker : Listener {
    var availableList = listOf<String>()

    private fun isAvailable(name: String) = availableList.contains(name)

    @EventHandler
    fun on(e: ServerConnectedEvent) {
        val player = e.player
        val modList = player.modList.keys
        val unavailableModList = modList.filterNot(ModBlocker::isAvailable)
        if (unavailableModList.isNotEmpty()) {
            plugin.proxy.console.send("&b[ModBlock] &a${player.name} &fは &a${unavailableModList.joinToString()} &fを導入していた為、ログインが制限されました")
            player.disconnect(
                buildJson {
                    appendln("&c&l許可されていない Mod が導入されています")
                }
            )
        }
    }
}
