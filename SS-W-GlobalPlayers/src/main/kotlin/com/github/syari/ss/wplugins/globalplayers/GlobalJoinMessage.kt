package com.github.syari.ss.wplugins.globalplayers

import com.github.syari.ss.wplugins.core.message.JsonBuilder.Companion.buildJson
import com.github.syari.ss.wplugins.globalplayers.Main.Companion.plugin
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

object GlobalJoinMessage : Listener {
    @EventHandler
    fun on(e: PostLoginEvent) {
        plugin.proxy.broadcast(
            buildJson {
                append("&7&l>> &a&lJoin &f&l${e.player.displayName}")
            }
        )
    }

    @EventHandler
    fun on(e: PlayerDisconnectEvent) {
        plugin.proxy.broadcast(
            buildJson {
                append("&7&l>> &c&lQuit &f&l${e.player.displayName}")
            }
        )
    }
}
