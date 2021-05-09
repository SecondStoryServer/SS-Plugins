package com.github.syari.ss.wplugins.discord.api.util.json

import com.google.gson.JsonArray
import com.google.gson.JsonElement

internal class JsonArrayBuilder {
    private val jsonArray = JsonArray()

    operator fun String?.unaryPlus() {
        jsonArray.add(this)
    }

    operator fun Boolean?.unaryPlus() {
        jsonArray.add(this)
    }

    operator fun JsonElement?.unaryPlus() {
        jsonArray.add(this)
    }

    fun get() = jsonArray
}
