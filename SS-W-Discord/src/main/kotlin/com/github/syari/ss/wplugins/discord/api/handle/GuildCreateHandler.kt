package com.github.syari.ss.wplugins.discord.api.handle

import com.github.syari.ss.wplugins.discord.api.entity.Guild
import com.github.syari.ss.wplugins.discord.api.util.json.getOrNull
import com.google.gson.JsonObject

internal object GuildCreateHandler : GatewayHandler {
    override fun handle(json: JsonObject) {
        if (json.getOrNull("unavailable")?.asBoolean == true) {
            return
        }

        val id = json["id"].asLong

        Guild.putOrUpdate(id, json)
    }
}
