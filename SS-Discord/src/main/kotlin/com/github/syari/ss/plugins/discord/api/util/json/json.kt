package com.github.syari.ss.plugins.discord.api.util.json

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

inline fun json(builder: JsonBuilder.() -> Unit) = JsonBuilder().apply(builder).get()

inline fun jsonArray(builder: JsonArrayBuilder.() -> Unit) = JsonArrayBuilder().apply(builder).get()

fun JsonObject.getOrNull(memberName: String): JsonElement? {
    return if (has(memberName)) get(memberName) else null
}

fun JsonObject.getObjectOrNull(memberName: String): JsonObject? {
    return getOrNull(memberName)?.let {
        if (it.isJsonObject) it.asJsonObject else null
    }
}

fun JsonObject.getArrayOrNull(memberName: String): JsonArray? {
    return getOrNull(memberName)?.let {
        return if (it.isJsonArray) it.asJsonArray else null
    }
}

val JsonElement.asStringOrNull
    get() = if (isJsonPrimitive && asJsonPrimitive.isString) asString else null