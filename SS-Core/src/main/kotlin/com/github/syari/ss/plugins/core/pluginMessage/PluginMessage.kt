package com.github.syari.ss.plugins.core.pluginMessage

import com.github.syari.ss.plugins.core.Main.Companion.corePlugin
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.wplugins.core.pluginMessage.template.PluginMessageTemplate
import com.google.common.io.ByteStreams
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener

object PluginMessage: OnEnable, PluginMessageListener {
    override fun onEnable() {
        corePlugin.server.messenger.registerIncomingPluginChannel(corePlugin, PluginMessageTemplate.ChannelName, this)
        corePlugin.server.messenger.registerOutgoingPluginChannel(corePlugin, PluginMessageTemplate.ChannelName)
    }

    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {
        if (channel.equals(PluginMessageTemplate.ChannelName, true)) {
            PluginMessageTemplate.readFrom(ByteStreams.newDataInput(message))?.let {
                SSPluginMessageEvent(it).callEvent()
            }
        }
    }

    fun send(template: PluginMessageTemplate) {
        val dataOutput = ByteStreams.newDataOutput()
        dataOutput.writeUTF(template.subChannel)
        template.writeTo(dataOutput)
        corePlugin.server.sendPluginMessage(corePlugin, PluginMessageTemplate.ChannelName, dataOutput.toByteArray())
    }
}