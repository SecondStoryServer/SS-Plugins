package com.github.syari.ss.plugins.discord.api.entity.api

import com.github.syari.ss.plugins.discord.api.entity.Mentionable

internal data class Emoji(val name: String, val id: Long, val isAnimated: Boolean): Mentionable {
    override val asMentionDisplay: String
        get() = ":$name:"

    override val asMentionRegex: Regex
        get() = "<${if (isAnimated) "a" else ""}:$name:$id>".toRegex()

    companion object {
        internal const val REGEX = "<a?:([a-zA-Z0-9_]+):([0-9]+)>"
    }
}