package com.github.syari.ss.plugins.core.scoreboard

import org.bukkit.plugin.java.JavaPlugin

object CreateScoreBoard {
    /**
     * スコアボードを作成します
     * @param plugin 作成するプラグイン
     * @param title タイトル
     * @param priority 優先度
     * @param run ボードに対して実行する処理
     * @return [CustomScoreBoard]
     */
    fun createScoreBoard(
        plugin: JavaPlugin, title: String, priority: ScoreBoardPriority, run: CustomScoreBoardEdit.() -> Unit
    ): CustomScoreBoard {
        return CustomScoreBoard(plugin, title, priority).apply(run)
    }
}