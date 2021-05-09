package com.github.syari.ss.wplugins.discord.api.handle

import com.google.gson.JsonObject

internal interface GatewayHandler {
    fun handle(json: JsonObject)
}
