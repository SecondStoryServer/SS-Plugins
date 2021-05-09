package com.github.syari.ss.wplugins.globalplayers

import com.github.syari.ss.template.message.PluginMessageTemplateTabList
import com.github.syari.ss.wplugins.core.message.JsonBuilder.Companion.buildJson
import com.github.syari.ss.wplugins.core.pluginMessage.PluginMessage
import com.github.syari.ss.wplugins.core.scheduler.CreateScheduler.runLater
import com.github.syari.ss.wplugins.globalplayers.Main.Companion.plugin
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.ServerConnectedEvent
import net.md_5.bungee.api.event.ServerDisconnectEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

object TabUpdater : Listener {
    private fun updateTabList() {
        plugin.runLater(5) {
            val playerList = plugin.proxy.players
            val playerNameList = playerList.map(ProxiedPlayer::getDisplayName)
            plugin.proxy.serversCopy.values.forEach {
                PluginMessage.send(it, PluginMessageTemplateTabList(playerNameList))
            }
            val header = buildJson {
                appendln("&8&l>> &6&lSecond Story Server &8&l<<")
                appendln()
                appendln("&6&lss-rpg.net")
                append("    &7&l&m---------------------------&r    ")
            }
            val footer = buildJson {
                appendln("    &7&l&m---------------------------&r    ")
                appendln()
                appendln("&6&l${playerList.size} / &6&l${plugin.proxy.config.listeners.first().maxPlayers}")
            }
            playerList.forEach {
                it.setTabHeader(header, footer)
            }
        }
    }

    @EventHandler
    fun on(e: ServerConnectedEvent) {
        updateTabList()
    }

    @EventHandler
    fun on(e: ServerDisconnectEvent) {
        updateTabList()
    }
}
