package com.github.syari.ss.plugins.discord.api.entity.enums

internal enum class ChannelType(private val id: Int) {
    GUILD_TEXT(0); // DM(1) // GUILD_VOICE(2)
    // GROUP_DM(3)
    // GUILD_CATEGORY(4)
    // GUILD_NEWS(5)
    // GUILD_STORE(6)

    companion object {
        fun get(id: Int): ChannelType? {
            return values().firstOrNull { it.id == id }
        }
    }
}