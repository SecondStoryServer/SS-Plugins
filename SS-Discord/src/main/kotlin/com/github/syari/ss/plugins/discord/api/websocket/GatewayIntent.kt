package com.github.syari.ss.plugins.discord.api.websocket

enum class GatewayIntent(val flag: Int) {
    /**
    - GUILD_CREATE
    - GUILD_DELETE
    - GUILD_ROLE_CREATE
    - GUILD_ROLE_UPDATE
    - GUILD_ROLE_DELETE
    - CHANNEL_CREATE
    - CHANNEL_UPDATE
    - CHANNEL_DELETE
    - CHANNEL_PINS_UPDATE
     */
    GUILDS(1 shl 0),

    /**
    - MESSAGE_CREATE
    - MESSAGE_UPDATE
    - MESSAGE_DELETE
     */
    GUILD_MESSAGES(1 shl 9)
}