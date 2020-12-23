package com.github.syari.ss.plugins.discord.api.util.json

import com.google.gson.JsonElement
import com.google.gson.JsonObject

class JsonBuilder {
    private val jsonObject = JsonObject()

    infix fun String.to(value: Number?) {
        jsonObject.addProperty(this, value)
    }

    infix fun String.to(value: String?) {
        jsonObject.addProperty(this, value)
    }

    infix fun String.to(value: JsonElement?) {
        jsonObject.add(this, value)
    }

    fun get() = jsonObject
}