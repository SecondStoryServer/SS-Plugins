package com.github.syari.ss.wplugins.chat.discord

import com.github.syari.ss.wplugins.chat.channel.ChatChannel
import com.github.syari.ss.wplugins.core.code.StringEditor.toUncolor
import com.github.syari.ss.wplugins.core.message.JsonBuilder
import com.github.syari.ss.wplugins.core.message.JsonBuilder.Companion.buildJson

class DiscordListenChannel(
    private val prefix: String,
    private val chatChannel: ChatChannel,
    private val messageMaxLength: Int?
) {
    fun send(name: String, message: String) {
        chatChannel.send(
            buildJson {
                append(
                    "$prefix${name.toUncolor}: ",
                    JsonBuilder.Hover.Text("&6Discord に参加する"),
                    JsonBuilder.Click.OpenURL(Discord.joinUrl.toString())
                )
                if (messageMaxLength != null && messageMaxLength < message.length) {
                    append(message.substring(0, messageMaxLength) + "...")
                } else {
                    append(message)
                }
            }
        )
        chatChannel.sendConsoleLog(name, message, true)
    }
}
