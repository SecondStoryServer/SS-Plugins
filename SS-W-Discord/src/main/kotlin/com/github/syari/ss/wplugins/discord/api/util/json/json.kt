package com.github.syari.ss.wplugins.discord.api.util.json

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

internal inline fun json(builder: JsonBuilder.() -> Unit) = JsonBuilder().apply(builder).get()

internal inline fun jsonArray(builder: JsonArrayBuilder.() -> Unit) = JsonArrayBuilder().apply(builder).get()

internal fun JsonObject.getOrNull(memberName: String): JsonElement? {
    return if (has(memberName)) get(memberName) else null
}

internal fun JsonObject.getObjectOrNull(memberName: String): JsonObject? {
    return getOrNull(memberName)?.let {
        if (it.isJsonObject) it.asJsonObject else null
    }
}

internal fun JsonObject.getArrayOrNull(memberName: String): JsonArray? {
    return getOrNull(memberName)?.let {
        return if (it.isJsonArray) it.asJsonArray else null
    }
}

internal val JsonElement.asStringOrNull
    get() = if (isJsonPrimitive && asJsonPrimitive.isString) asString else null
