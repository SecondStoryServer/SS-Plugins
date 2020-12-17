package com.github.syari.ss.plugins.core.message

import com.github.syari.ss.plugins.core.Main.Companion.corePlugin
import com.github.syari.ss.plugins.core.code.StringEditor.toColor
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object Message {
    /**
     * プレイヤー全員とコンソールにメッセージを送信します
     * @param message 送信するメッセージ
     */
    fun broadcast(message: String) {
        val coloredMessage = message.toColor
        corePlugin.server.broadcastMessage(coloredMessage)
    }

    /**
     * メッセージを複数のプレイヤーに送信します
     * @param message 本文
     * @param to 送信先
     */
    fun send(
        message: String, vararg to: CommandSender
    ) {
        val colored = message.toColor
        to.forEach {
            it.sendMessage(colored)
        }
    }

    /**
     * メッセージを複数のプレイヤーに送信します
     * @param message 本文
     * @param to 送信先
     */
    fun send(
        message: TextComponent, vararg to: CommandSender
    ) {
        to.forEach {
            it.sendMessage(message)
        }
    }

    /**
     * メッセージを送信します
     * @param message 本文
     */
    fun CommandSender.send(message: String) {
        send(message, this)
    }

    /**
     * メッセージを送信します
     * @param message 本文
     */
    fun CommandSender.send(message: TextComponent) {
        send(message, this)
    }

    /**
     * メッセージを送信します
     * @param builder 本文
     */
    fun CommandSender.send(builder: StringBuilder) {
        send(builder.toString())
    }

    /**
     * タイトルを表示します
     * @param main メインタイトル
     * @param sub サブタイトル
     * @param fadeIn フェードイン時間
     * @param stay 表示時間
     * @param fadeOut フェードアウト時間
     */
    fun Player.title(
        main: String, sub: String, fadeIn: Int, stay: Int, fadeOut: Int
    ) {
        sendTitle(main.toColor, sub.toColor, fadeIn, stay, fadeOut)
    }

    /**
     * アクションバーにメッセージを表示します
     * @param message 本文
     */
    fun Player.action(message: String) {
        sendActionBar(message.toColor)
    }
}