package com.github.syari.ss.plugins.core.scoreboard

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

object CreateScoreBoard {
    /**
     * スコアボードを作成します
     * @param title タイトル
     * @param priority 優先度
     * @param action ボードの内容
     * @return [CustomScoreBoard]
     */
    fun JavaPlugin.board(
        title: String, priority: Int, action: (Player) -> String
    ): CustomScoreBoard {
        return CustomScoreBoard(this, title, priority, action)
    }
}