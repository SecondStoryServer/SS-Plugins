package com.github.syari.ss.plugins.discord.api.handle

import com.github.syari.ss.plugins.discord.api.entity.Guild
import com.github.syari.ss.plugins.discord.api.util.json.JsonUtil.getOrNull
import com.google.gson.JsonObject

object GuildCreateHandler: GatewayHandler {
    override fun handle(json: JsonObject) {
        if (json.getOrNull("unavailable")?.asBoolean == true) {
            return
        }

        val id = json["id"].asLong

        Guild.putOrUpdate(id, json)
    }
}