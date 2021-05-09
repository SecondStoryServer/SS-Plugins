package com.github.syari.ss.wplugins.core.command

import com.github.syari.ss.wplugins.core.Main.Companion.plugin
import net.md_5.bungee.api.connection.ProxiedPlayer

class CommandExecuteArgument(array: Array<out String>) : CommandArgument(array) {
    internal lateinit var executeAction: CommandExecuteAction

    /**
     * 指定した要素をプレイヤーに変換します
     * @param index 取得する位置
     * @param equalName 名前が完全一致した場合のみ取得する
     * @return [ProxiedPlayer]?
     */
    override fun getPlayer(
        index: Int,
        equalName: Boolean
    ): ProxiedPlayer? {
        val rawPlayer = getOrNull(index) ?: return run {
            executeAction.sendError(ErrorMessage.NotEnterPlayer)
            null
        }
        val player = plugin.proxy.getPlayer(rawPlayer)
        return if (player != null && (!equalName || player.name.equals(rawPlayer, ignoreCase = true))) {
            player
        } else {
            executeAction.sendError(ErrorMessage.NotFoundPlayer)
            null
        }
    }
}
