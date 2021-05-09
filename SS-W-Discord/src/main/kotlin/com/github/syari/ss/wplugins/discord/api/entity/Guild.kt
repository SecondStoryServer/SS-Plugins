package com.github.syari.ss.wplugins.discord.api.entity

import com.github.syari.ss.wplugins.discord.api.entity.TextChannel.Companion.allTextChannels
import com.github.syari.ss.wplugins.discord.api.util.json.getOrNull
import com.google.gson.JsonObject

internal class Guild {
    private val emojis = mutableMapOf<Long, Emoji>()
    private val roles = mutableMapOf<Long, Role>()
    private val textChannels = mutableMapOf<Long, TextChannel>()

    private fun update(json: JsonObject) {
        updateEmoji(json)
        updateRole(json)
        updateChannel(json)
    }

    private fun updateEmoji(json: JsonObject) {
        val emojiJsons = json["emojis"].asJsonArray.map { it.asJsonObject }
        emojiJsons.forEach {
            val emojiName = it["name"].asString
            val emojiId = it["id"].asLong
            val isAnimated = it["animated"].asBoolean
            emojis[emojiId] = Emoji(emojiName, emojiId, isAnimated)
        }
    }

    private fun updateRole(json: JsonObject) {
        val roleJsons = json["roles"].asJsonArray.map { it.asJsonObject }
        roleJsons.forEach {
            val roleName = it["name"].asString
            val roleId = it["id"].asLong
            roles[roleId] = Role(roleName, roleId)
        }
    }

    private fun updateChannel(json: JsonObject) {
        val channelJsons = json.getOrNull("channels")?.asJsonArray?.map { it.asJsonObject }
        channelJsons?.forEach {
            when (ChannelType.get(it["type"].asInt)) {
                ChannelType.GUILD_TEXT -> {
                    val channelId = it["id"].asLong
                    val channelName = it["name"].asString
                    val channel = TextChannel(channelName, channelId)
                    textChannels[channelId] = channel
                    allTextChannels[channelId] = channel
                }
            }
        }
    }

    fun getTextChannel(id: Long): TextChannel? {
        return textChannels[id]
    }

    fun getRole(id: Long): Role? {
        return roles[id]
    }

    fun getEmoji(id: Long): Emoji? {
        return emojis[id]
    }

    companion object {
        private val serverList = mutableMapOf<Long, Guild>()

        fun get(id: Long): Guild? {
            return serverList[id]
        }

        fun putOrUpdate(id: Long, json: JsonObject) {
            serverList.getOrPut(id) { Guild() }.update(json)
        }
    }
}
