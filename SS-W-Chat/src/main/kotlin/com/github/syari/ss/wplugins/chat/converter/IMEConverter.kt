package com.github.syari.ss.wplugins.chat.converter

import com.google.gson.Gson
import com.google.gson.JsonArray
import java.io.FileNotFoundException
import java.net.URL
import java.net.URLEncoder

object IMEConverter {
    private const val GOOGLE_IME_URL = "https://www.google.com/transliterate?langpair=ja-Hira|ja&text="

    fun String.toIME(): String {
        val url = GOOGLE_IME_URL + URLEncoder.encode(this, "UTF-8")
        return try {
            val json = URL(url).readText(Charsets.UTF_8)
            parseJson(json).ifEmpty { this }
        } catch (ex: FileNotFoundException) {
            this
        }
    }

    private fun parseJson(json: String): String {
        return buildString {
            for (response in Gson().fromJson(json, JsonArray::class.java)) {
                append(response.asJsonArray.get(1).asJsonArray.get(0).asString)
            }
        }
    }
}
