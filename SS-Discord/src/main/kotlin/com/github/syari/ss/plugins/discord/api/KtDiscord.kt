package com.github.syari.ss.plugins.discord.api

import com.github.syari.ss.plugins.discord.api.entity.api.Message
import com.github.syari.ss.plugins.discord.api.rest.EndPoint
import com.github.syari.ss.plugins.discord.api.rest.RestClient
import com.github.syari.ss.plugins.discord.api.websocket.GatewayClient
import com.github.syari.ss.plugins.discord.api.websocket.GatewayIntent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object KtDiscord {
    const val NAME = "KtDiscord"
    const val VERSION = "1.0.1"
    const val GITHUB_URL = "https://github.com/sya-ri/KtDiscord"
    const val API_VERSION = 6

    internal val LOGGER: Logger = LoggerFactory.getLogger(NAME)

    init {
        LOGGER.info("$NAME v$VERSION ($GITHUB_URL)")
    }

    internal lateinit var token: String
    internal const val shard = 0
    internal const val maxShards = 1
    internal val gatewayIntents = setOf(GatewayIntent.GUILDS, GatewayIntent.GUILD_MESSAGES)
    internal lateinit var messageReceiveEvent: (Message) -> Unit
    internal var status = ConnectStatus.DISCONNECTED

    suspend fun login(token: String, messageReceiveEvent: (Message) -> Unit) {
        if (status != ConnectStatus.DISCONNECTED) {
            throw IllegalStateException()
        }
        if (token.isBlank()) {
            throw IllegalArgumentException("")
        }
        KtDiscord.token = token

        status = ConnectStatus.CONNECTING

        val gatewayURL = RestClient.request(EndPoint.GetGatewayBot).asJsonObject["url"].asString
        GatewayClient.connect(gatewayURL)

        status = ConnectStatus.CONNECTED

        KtDiscord.messageReceiveEvent = messageReceiveEvent
    }

    fun loginAsync(token: String, messageReceiveEvent: (Message) -> Unit) {
        GlobalScope.launch {
            login(token, messageReceiveEvent)
        }
    }
}