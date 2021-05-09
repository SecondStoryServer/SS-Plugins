package com.github.syari.ss.wplugins.chat.channel

import com.github.syari.ss.wplugins.chat.ChatTemplate
import com.github.syari.ss.wplugins.core.player.UUIDPlayer
import com.github.syari.ss.wplugins.discord.Discord

class ChatChannelOption(discordChannelId: Long?, val templateDiscord: ChatTemplate, val prefix: String?, val players: List<UUIDPlayer>) {
    companion object {
        var list = mapOf<Regex, ChatChannelOption>()

        @OptIn(ExperimentalStdlibApi::class)
        fun get(name: String) = buildList {
            list.forEach {
                if (it.key.matches(name)) {
                    add(it.value)
                }
            }
        }
    }

    val discordChannel by lazy { discordChannelId?.let(Discord::getTextChannel) }
}
