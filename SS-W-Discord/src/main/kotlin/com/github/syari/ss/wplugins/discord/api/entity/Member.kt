package com.github.syari.ss.wplugins.discord.api.entity

import com.github.syari.ss.wplugins.discord.api.util.json.asStringOrNull
import com.github.syari.ss.wplugins.discord.api.util.json.getOrNull
import com.google.gson.JsonObject

data class Member(
    val name: String,
    val id: Long,
    val isBot: Boolean,
    val nickName: String?
) : Mentionable {
    val displayName
        get() = nickName ?: name

    override val asMentionDisplay: String
        get() = "@$displayName"

    override val asMentionRegex: Regex
        get() = "<@!?$id>".toRegex()

    internal companion object {
        fun from(memberJson: JsonObject?, userJson: JsonObject): Member {
            val user = User.from(userJson)
            val name = user.name
            val id = user.id
            val isBot = user.isBot
            val nickName = memberJson?.getOrNull("nick")?.asStringOrNull
            return Member(name, id, isBot, nickName)
        }
    }
}
