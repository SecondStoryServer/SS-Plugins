package com.github.syari.ss.plugins.discord

import com.github.syari.ss.plugins.discord.api.DiscordAPI

object Discord {
    fun login(token: String) {
        DiscordAPI.login(token) {
            DiscordMessageReceiveEvent(it).callEvent()
        }
    }
}