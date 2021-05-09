package com.github.syari.ss.wplugins.discord

import com.github.syari.ss.wplugins.discord.api.DiscordAPI
import com.github.syari.ss.wplugins.discord.api.entity.TextChannel

object Discord {
    fun login(token: String) {
        DiscordAPI.login(token) {
            DiscordMessageReceiveEvent(it).callEvent()
        }
    }

    val connectStatus
        get() = DiscordAPI.status

    fun getTextChannel(id: Long) = TextChannel.get(id)
}
