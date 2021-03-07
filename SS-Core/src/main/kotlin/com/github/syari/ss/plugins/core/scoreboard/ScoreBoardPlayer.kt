@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.scoreboard

import com.github.syari.spigot.api.uuid.UUIDPlayer
import org.bukkit.scoreboard.DisplaySlot

internal class ScoreBoardPlayer(val uuidPlayer: UUIDPlayer) {
    companion object {
        private val playerList = mutableMapOf<UUIDPlayer, ScoreBoardPlayer>()

        /**
         * スコアボードのプレイヤーデータ
         */
        internal val UUIDPlayer.scoreBoardPlayer
            get() = playerList.getOrPut(this) { ScoreBoardPlayer(this) }
    }

    private val boardList = mutableSetOf<CustomScoreBoard>()

    var board: CustomScoreBoard? = null
        private set

    /**
     * ボードを変更します
     * @param board スコアボード
     */
    private fun setBoard(board: CustomScoreBoard?) {
        board?.show(this) ?: uuidPlayer.player?.scoreboard?.clearSlot(DisplaySlot.SIDEBAR)
        this.board = board
    }

    private fun updateBoard() {
        val lastBoard = board
        val board = boardList.maxByOrNull { it.priority }
        if (lastBoard != board) {
            setBoard(board)
        }
    }

    fun updateBoard(board: CustomScoreBoard) {
        if (board == this.board) {
            board.show(this)
        }
    }

    /**
     * ボードを追加します
     * @param board スコアボード
     */
    fun addBoard(board: CustomScoreBoard) {
        boardList.add(board)
        updateBoard()
    }

    /**
     * ボードを削除します
     * @param board スコアボード
     */
    fun removeBoard(board: CustomScoreBoard) {
        boardList.remove(board)
        updateBoard()
    }
}
