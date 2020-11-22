package com.github.syari.ss.plugins.core.scoreboard

import com.github.syari.ss.plugins.core.player.UUIDPlayer
import org.bukkit.scoreboard.DisplaySlot

data class ScoreBoardPlayer(val uuidPlayer: UUIDPlayer) {
    companion object {
        private val playerList = mutableMapOf<UUIDPlayer, ScoreBoardPlayer>()

        /**
         * ボードを追加します
         * @param uuidPlayer 対象プレイヤー
         * @param board スコアボード
         */
        fun addBoard(
            uuidPlayer: UUIDPlayer,
            board: CustomScoreBoard
        ) {
            playerList.getOrPut(uuidPlayer) { ScoreBoardPlayer(uuidPlayer) }.addBoard(board)
        }

        /**
         * ボードを削除します
         * @param uuidPlayer 対象プレイヤー
         * @param board スコアボード
         */
        fun removeBoard(
            uuidPlayer: UUIDPlayer,
            board: CustomScoreBoard
        ) {
            playerList[uuidPlayer]?.removeBoard(board)
        }

        /**
         * ボードを全て削除します
         * @param uuidPlayer 対象プレイヤー
         */
        fun clearBoard(uuidPlayer: UUIDPlayer) {
            playerList.remove(uuidPlayer)
        }
    }

    private val boardList = mutableSetOf<CustomScoreBoard>()

    var board: CustomScoreBoard? = null
        private set

    /**
     * ボードを変更します
     * @param board スコアボード
     */
    fun setBoard(board: CustomScoreBoard?) {
        board?.show(this) ?: uuidPlayer.player?.scoreboard?.clearSlot(DisplaySlot.SIDEBAR)
        this.board = board
    }

    private fun updateBoard() {
        val lastBoard = board
        val board = boardList.maxByOrNull { it.priority.level }
        if (lastBoard != board) {
            setBoard(board)
        }
    }

    /**
     * ボードを再表示します
     * @param board スコアボード
     */
    fun updateBoard(board: CustomScoreBoard) {
        if (this.board == board) {
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
        if (boardList.isEmpty()) {
            clearBoard(uuidPlayer)
        } else {
            updateBoard()
        }
    }
}