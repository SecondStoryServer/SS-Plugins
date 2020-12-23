package com.github.syari.ss.plugins.discord.api.entity.api

import com.github.syari.ss.plugins.discord.api.entity.Mentionable
import com.github.syari.ss.plugins.discord.api.util.json.JsonUtil.asStringOrNull
import com.github.syari.ss.plugins.discord.api.util.json.JsonUtil.getOrNull
import com.google.gson.JsonObject

data class Member internal constructor(
    val name: String, val id: Long, val isBot: Boolean, val nickName: String?
): Mentionable {
    val displayName
        get() = nickName ?: name

    override val asMentionDisplay: String
        get() = "@$displayName"

    override val asMentionRegex: Regex
        get() = "<@!?$id>".toRegex()

    internal companion object {
        fun from(memberJson: JsonObject, userJson: JsonObject): Member {
            val user = User.from(userJson)
            val name = user.name
            val id = user.id
            val isBot = user.isBot
            val nickName = memberJson.getOrNull("nick")?.asStringOrNull
            return Member(name, id, isBot, nickName)
        }
    }
}