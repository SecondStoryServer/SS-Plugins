package com.github.syari.ss.plugins.discord.api.handle

import com.google.gson.JsonObject

internal interface GatewayHandler {
    fun handle(json: JsonObject)
}