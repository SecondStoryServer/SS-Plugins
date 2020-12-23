package com.github.syari.ss.plugins.discord.api

import com.github.syari.ss.plugins.discord.api.entity.Message
import com.github.syari.ss.plugins.discord.api.rest.EndPoint
import com.github.syari.ss.plugins.discord.api.rest.RestClient
import com.github.syari.ss.plugins.discord.api.websocket.GatewayClient
import com.google.gson.Gson
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object DiscordAPI {
    const val NAME = "DiscordAPI"
    const val GITHUB_URL = "https://github.com/SecondStoryServer/SS-Plugins"
    const val API_VERSION = 6

    val LOGGER: Logger = LoggerFactory.getLogger(NAME)
    val GSON = Gson()

    init {
        LOGGER.info("$NAME ($GITHUB_URL)")
    }

    lateinit var token: String
    lateinit var messageReceiveEvent: (Message) -> Unit
    var status = ConnectStatus.DISCONNECTED

    fun login(token: String, messageReceiveEvent: (Message) -> Unit) {
        if (status != ConnectStatus.DISCONNECTED) throw IllegalStateException()
        if (token.isBlank()) throw IllegalArgumentException("")
        this.token = token
        this.messageReceiveEvent = messageReceiveEvent
        status = ConnectStatus.CONNECTING
        GatewayClient.connect(RestClient.request(EndPoint.GetGatewayBot).asJsonObject["url"].asString)
        status = ConnectStatus.CONNECTED
    }
}