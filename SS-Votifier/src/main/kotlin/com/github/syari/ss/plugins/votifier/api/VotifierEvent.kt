package com.github.syari.ss.plugins.votifier.api

import com.github.syari.ss.plugins.core.event.CustomEvent
import com.github.syari.ss.plugins.votifier.Main

/**
 * 投票時に発生するイベントです
 * @param serviceName 投票元
 * @param username ユーザー名
 */
class VotifierEvent(
    val serviceName: String,
    val username: String
): CustomEvent() {
    /**
     * ユーザー名から取得したプレイヤー
     */
    @Suppress("DEPRECATION")
    val user by lazy {
        val player = Main.plugin.server.getOfflinePlayer(username)
        if (player.name.equals(username, true)) player else null
    }
}