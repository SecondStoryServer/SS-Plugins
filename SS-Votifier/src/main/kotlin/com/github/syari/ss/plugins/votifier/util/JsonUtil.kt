package com.github.syari.ss.plugins.votifier.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject

object JsonUtil {
    private val gson: Gson = GsonBuilder().create()

    /**
     * 文字列に変換します
     * @param jsonObject Json
     * @return [String]
     */
    fun toJson(jsonObject: JsonObject): String {
        return gson.toJson(jsonObject)
    }

    /**
     * Jsonに変換します
     * @param text 文字列
     * @return [JsonObject]
     */
    fun fromJson(text: String): JsonObject {
        return gson.fromJson(text, JsonObject::class.java)
    }
}