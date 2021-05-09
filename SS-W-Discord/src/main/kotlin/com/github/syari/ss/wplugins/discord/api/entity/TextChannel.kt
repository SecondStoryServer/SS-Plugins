package com.github.syari.ss.wplugins.discord.api.entity

import com.github.syari.ss.wplugins.discord.api.rest.EndPoint
import com.github.syari.ss.wplugins.discord.api.rest.RestClient
import com.github.syari.ss.wplugins.discord.api.util.json.json

/**
 * GuildTextChannel
 */
data class TextChannel(val name: String, val id: Long) : Mentionable {
    fun send(message: String) {
        RestClient.request(
            EndPoint.CreateMessage(id),
            json {
                "content" to message
            }
        )
    }

    override val asMentionDisplay: String
        get() = "#$name"

    override val asMentionRegex: Regex
        get() = "<#$id>".toRegex()

    internal companion object {
        val allTextChannels = mutableMapOf<Long, TextChannel>()

        fun get(id: Long): TextChannel? {
            return allTextChannels[id]
        }

        const val REGEX = "<#(\\d+)>"
    }
}
