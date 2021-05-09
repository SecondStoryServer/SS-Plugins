package com.github.syari.ss.wplugins.accessblocker

import com.github.syari.ss.wplugins.accessblocker.Main.Companion.plugin
import com.github.syari.ss.wplugins.core.message.JsonBuilder.Companion.buildJson
import com.github.syari.ss.wplugins.core.message.Message.send
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.PluginMessageEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

object BrandBlocker : Listener {
    var availableList = listOf<String>()

    private fun isAvailable(name: String) = availableList.contains(name)

    @EventHandler
    fun on(e: PluginMessageEvent) {
        val player = e.sender
        if (player is ProxiedPlayer && e.tag == "minecraft:brand") {
            val length = e.data[0]
            val brandName = e.data.slice(1..length).toByteArray().toString(Charsets.UTF_8)
            if (isAvailable(brandName).not()) {
                plugin.proxy.console.send("&b[BrandBlock] &a${player.name} &fは &a$brandName &fを使用していた為、ログインが制限されました")
                player.disconnect(
                    buildJson {
                        appendln("&c&l許可されていないクライアントを使用しています")
                    }
                )
            }
        }
    }
}
