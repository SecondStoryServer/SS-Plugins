package com.github.syari.ss.plugins.discord.api.entity.api

import com.github.syari.ss.plugins.discord.api.entity.Mentionable
import com.github.syari.ss.plugins.discord.api.rest.EndPoint
import com.github.syari.ss.plugins.discord.api.rest.RestClient
import com.github.syari.ss.plugins.discord.api.util.json.JsonUtil.json
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * GuildTextChannel
 */
data class TextChannel internal constructor(val name: String, val id: Long): Mentionable {
    fun send(message: String) {
        GlobalScope.launch {
            RestClient.request(EndPoint.CreateMessage(id), json {
                "content" to message
            })
        }
    }

    override val asMentionDisplay: String
        get() = "#$name"

    override val asMentionRegex: Regex
        get() = "<#$id>".toRegex()

    companion object {
        internal val allTextChannels = mutableMapOf<Long, TextChannel>()

        fun get(id: Long): TextChannel? {
            return allTextChannels[id]
        }

        internal const val REGEX = "<#(\\d+)>"
    }
}