package com.github.syari.ss.plugins.discord.api.entity

import com.github.syari.ss.plugins.discord.api.util.json.getOrNull
import com.google.gson.JsonObject

data class User(val name: String, val id: Long, val isBot: Boolean) {
    internal companion object {
        fun from(json: JsonObject): User {
            val name: String = json["username"].asString
            val id = json["id"].asLong
            val isBot = json.getOrNull("bot")?.asBoolean ?: false
            return User(name, id, isBot)
        }
    }
}