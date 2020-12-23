package com.github.syari.ss.plugins.discord.api.handle

import com.github.syari.ss.plugins.discord.api.DiscordAPI.LOGGER
import com.google.gson.JsonObject

internal object EventHandler {
    private val handlers = mapOf(
        "GUILD_CREATE" to GuildCreateHandler, "MESSAGE_CREATE" to MessageCreateHandler
    )

    fun handleEvent(eventType: String, json: JsonObject) {
        try {
            handlers[eventType]?.handle(json)
        } catch (ex: Exception) {
            LOGGER.error("Failed to handle the event! (type: $eventType, json: $json)", ex)
        }
    }
}