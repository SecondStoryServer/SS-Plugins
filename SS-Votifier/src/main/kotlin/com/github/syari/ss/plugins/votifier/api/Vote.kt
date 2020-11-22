package com.github.syari.ss.plugins.votifier.api

import com.google.gson.JsonObject

/**
 * 投票データ
 * @param serviceName 投票元
 * @param username プレイヤー名
 */
data class Vote(
    val serviceName: String,
    val username: String
) {
    companion object {
        /**
         * Jsonを投票データに変更します
         * @param jsonObject Json
         * @return [Vote]?
         */
        fun from(jsonObject: JsonObject): Vote? {
            return try {
                val serviceName = jsonObject["serviceName"].asString
                val userName = jsonObject["username"].asString
                Vote(serviceName, userName)
            } catch (ex: Exception) {
                null
            }
        }
    }

}