package com.github.syari.ss.plugins.discord

import com.github.syari.ss.plugins.discord.api.DiscordAPI
import com.github.syari.ss.plugins.discord.api.entity.TextChannel

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