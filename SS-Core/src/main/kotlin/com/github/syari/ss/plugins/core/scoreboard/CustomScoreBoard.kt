@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.scoreboard

import com.github.syari.spigot.api.util.uuid.UUIDPlayer
import com.github.syari.ss.plugins.core.code.StringEditor.toColor
import com.github.syari.ss.plugins.core.scoreboard.CreateScoreBoard.board
import com.github.syari.ss.plugins.core.scoreboard.ScoreBoardPlayer.Companion.scoreBoardPlayer
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scoreboard.DisplaySlot

/**
 * @see CreateScoreBoard.board
 */
class CustomScoreBoard internal constructor(
    private val plugin: JavaPlugin,
    private val title: String,
    val priority: Int,
    private val action: (Player) -> String
) {
    /**
     * スコアボードの再表示をします
     * @param players プレイヤー
     */
    fun updatePlayer(vararg players: Player) {
        players.forEach { UUIDPlayer.from(it).scoreBoardPlayer.updateBoard(this) }
    }

    /**
     * 表示するプレイヤーを追加します
     * @param players プレイヤー
     */
    fun addPlayer(vararg players: Player) {
        players.forEach { UUIDPlayer.from(it).scoreBoardPlayer.addBoard(this) }
    }

    /**
     * 表示するプレイヤーを削除します
     * @param players プレイヤー
     */
    fun removePlayer(vararg players: Player) {
        players.forEach { UUIDPlayer.from(it).scoreBoardPlayer.removeBoard(this) }
    }

    /**
     * スコアボードを表示します
     * @param scoreBoardPlayer プレイヤー
     */
    internal fun show(scoreBoardPlayer: ScoreBoardPlayer) {
        val player = scoreBoardPlayer.uuidPlayer.player ?: return
        val board = plugin.server.scoreboardManager.newScoreboard
        val boardName = player.uniqueId.toString().substring(0 until 16)
        val scoreboard = board.registerNewObjective(boardName, "dummy", title.toColor).apply {
            displaySlot = DisplaySlot.SIDEBAR
            var lineNumber = 0
            action(player).lines().forEachIndexed { index, text ->
                getScore((text + "&" + "%x".format(lineNumber)).toColor).score = -index
                lineNumber++
            }
        }.scoreboard ?: return
        if (player.scoreboard.entries != scoreboard.entries) {
            player.scoreboard = scoreboard
        }
    }
}
