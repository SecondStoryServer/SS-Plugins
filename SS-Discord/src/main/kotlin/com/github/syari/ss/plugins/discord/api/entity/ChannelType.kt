package com.github.syari.ss.plugins.discord.api.entity

internal enum class ChannelType(private val id: Int) {
    GUILD_TEXT(0);

    companion object {
        fun get(id: Int): ChannelType? {
            return values().firstOrNull { it.id == id }
        }
    }
}