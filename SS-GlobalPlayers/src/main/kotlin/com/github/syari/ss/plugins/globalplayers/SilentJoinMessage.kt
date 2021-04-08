package com.github.syari.ss.plugins.globalplayers

import com.github.syari.spigot.api.event.events
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.globalplayers.Main.Companion.plugin
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object SilentJoinMessage : OnEnable {
    override fun onEnable() {
        plugin.events {
            event<PlayerJoinEvent> {
                it.joinMessage = null
            }
            event<PlayerQuitEvent> {
                it.quitMessage = null
            }
        }
    }
}
