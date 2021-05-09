package com.github.syari.ss.wplugins.discord.api.entity

data class Role(val name: String, val id: Long) : Mentionable {
    override val asMentionDisplay: String
        get() = "@$name"

    override val asMentionRegex: Regex
        get() = "<@&$id>".toRegex()
}
