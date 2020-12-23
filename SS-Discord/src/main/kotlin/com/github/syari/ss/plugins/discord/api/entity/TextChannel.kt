package com.github.syari.ss.plugins.discord.api.entity

import com.github.syari.ss.plugins.discord.api.rest.EndPoint
import com.github.syari.ss.plugins.discord.api.rest.RestClient
import com.github.syari.ss.plugins.discord.api.util.json.JsonUtil.json

/**
 * GuildTextChannel
 */
data class TextChannel(val name: String, val id: Long): Mentionable {
    fun send(message: String) {
        RestClient.request(EndPoint.CreateMessage(id), json {
            "content" to message
        })
    }

    override val asMentionDisplay: String
        get() = "#$name"

    override val asMentionRegex: Regex
        get() = "<#$id>".toRegex()

    companion object {
        val allTextChannels = mutableMapOf<Long, TextChannel>()

        fun get(id: Long): TextChannel? {
            return allTextChannels[id]
        }

        const val REGEX = "<#(\\d+)>"
    }
}