package com.github.syari.ss.plugins.discord.api.entity.api

import com.github.syari.ss.plugins.discord.api.entity.Mentionable

internal data class Role(val name: String, val id: Long): Mentionable {
    override val asMentionDisplay: String
        get() = "@$name"

    override val asMentionRegex: Regex
        get() = "<@&$id>".toRegex()
}