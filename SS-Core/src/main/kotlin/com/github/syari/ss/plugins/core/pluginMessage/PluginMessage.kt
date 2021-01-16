@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.pluginMessage

import com.github.syari.ss.plugins.core.Main.Companion.plugin
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.template.message.PluginMessageTemplate
import com.google.common.io.ByteStreams
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener

object PluginMessage : OnEnable, PluginMessageListener {
    override fun onEnable() {
        plugin.server.messenger.registerIncomingPluginChannel(plugin, PluginMessageTemplate.ChannelName, this)
        plugin.server.messenger.registerOutgoingPluginChannel(plugin, PluginMessageTemplate.ChannelName)
    }

    @Suppress("UnstableApiUsage")
    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {
        if (channel.equals(PluginMessageTemplate.ChannelName, true)) {
            PluginMessageTemplate.readFrom(ByteStreams.newDataInput(message))?.let {
                SSPluginMessageEvent(it).callEvent()
            }
        }
    }

    @Suppress("UnstableApiUsage")
    fun send(template: PluginMessageTemplate) {
        val dataOutput = ByteStreams.newDataOutput()
        dataOutput.writeUTF(template.subChannel)
        template.writeTo(dataOutput)
        plugin.server.sendPluginMessage(plugin, PluginMessageTemplate.ChannelName, dataOutput.toByteArray())
    }
}
