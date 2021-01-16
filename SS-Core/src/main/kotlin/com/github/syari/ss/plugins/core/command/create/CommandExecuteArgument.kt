@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.command.create

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class CommandExecuteArgument(array: Array<out String>) : CommandArgument(array) {
    internal lateinit var executeAction: CommandExecuteAction

    /**
     * 指定した要素をオフラインプレイヤーに変換します
     * @param index 取得する位置
     * @param equalName 名前が完全一致した場合のみ取得する
     * @return [OfflinePlayer]?
     */
    override fun getOfflinePlayer(
        index: Int,
        equalName: Boolean
    ): OfflinePlayer? {
        val rawPlayer = getOrNull(index) ?: return run {
            executeAction.sendError(ErrorMessage.NotEnterPlayer)
            null
        }
        @Suppress("DEPRECATION") val player = Bukkit.getOfflinePlayer(rawPlayer)
        return if (!equalName || player.name.equals(rawPlayer, ignoreCase = true)) {
            player
        } else {
            executeAction.sendError(ErrorMessage.NotFoundPlayer)
            null
        }
    }

    /**
     * 指定した要素をプレイヤーに変換します
     * @param index 取得する位置
     * @param equalName 名前が完全一致した場合のみ取得する
     * @return [Player]?
     */
    override fun getPlayer(
        index: Int,
        equalName: Boolean
    ): Player? {
        val rawPlayer = getOrNull(index) ?: return run {
            executeAction.sendError(ErrorMessage.NotEnterPlayer)
            null
        }
        val player = Bukkit.getPlayer(rawPlayer)
        return if (player != null && (!equalName || player.name.equals(rawPlayer, ignoreCase = true))) {
            player
        } else {
            executeAction.sendError(ErrorMessage.NotFoundPlayer)
            null
        }
    }
}
