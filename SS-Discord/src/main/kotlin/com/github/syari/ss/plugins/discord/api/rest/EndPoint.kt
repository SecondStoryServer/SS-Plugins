package com.github.syari.ss.plugins.discord.api.rest

internal sealed class EndPoint(val method: HttpMethod, val path: String) {
    object GetGatewayBot: EndPoint(HttpMethod.Get, "/gateway/bot")
    class CreateMessage(channel_id: Long): EndPoint(HttpMethod.Post, "/channels/$channel_id/messages")
}