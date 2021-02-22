@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.pluginMessage

import com.github.syari.ss.plugins.core.Main.Companion.plugin
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.template.message.PluginMessageTemplate
import com.google.common.io.ByteArrayDataOutput
import com.google.common.io.ByteStreams
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener

object PluginMessage : OnEnable, PluginMessageListener {
    private const val BungeeCord = "BungeeCord"

    override fun onEnable() {
        plugin.server.messenger.registerIncomingPluginChannel(plugin, PluginMessageTemplate.ChannelName, this)
        plugin.server.messenger.registerOutgoingPluginChannel(plugin, PluginMessageTemplate.ChannelName)
        plugin.server.messenger.registerOutgoingPluginChannel(plugin, BungeeCord)
    }

    @Suppress("UnstableApiUsage")
    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {
        if (channel.equals(PluginMessageTemplate.ChannelName, true)) {
            PluginMessageTemplate.readFrom(ByteStreams.newDataInput(message))?.let {
                SSPluginMessageEvent(it).callEvent()
            }
        }
    }

    fun send(template: PluginMessageTemplate, player: Player? = null) {
        send(PluginMessageTemplate.ChannelName, player) {
            writeUTF(template.subChannel)
            template.writeTo(this)
        }
    }

    fun sendBungee(player: Player? = null, action: ByteArrayDataOutput.() -> Unit) {
        send(BungeeCord, player, action)
    }

    @Suppress("UnstableApiUsage")
    private fun send(channelName: String, player: Player? = null, action: ByteArrayDataOutput.() -> Unit) {
        val dataOutput = ByteStreams.newDataOutput().apply(action)
        (player ?: plugin.server).sendPluginMessage(plugin, channelName, dataOutput.toByteArray())
    }
}
