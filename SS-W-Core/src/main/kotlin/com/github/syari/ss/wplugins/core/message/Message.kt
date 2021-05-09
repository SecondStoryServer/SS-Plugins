package com.github.syari.ss.wplugins.core.message

import com.github.syari.ss.wplugins.core.Main.Companion.plugin
import com.github.syari.ss.wplugins.core.code.StringEditor.toColor
import com.github.syari.ss.wplugins.core.code.StringEditor.toComponent
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer

object Message {
    /**
     * プレイヤー全員とコンソールにメッセージを送信します
     * @param message 送信するメッセージ
     */
    fun broadcast(message: String) {
        val component = message.toColor.toComponent
        plugin.proxy.broadcast(component)
    }

    /**
     * メッセージを複数のプレイヤーに送信します
     * @param message 本文
     * @param to 送信先
     */
    fun send(
        message: String,
        vararg to: CommandSender
    ) {
        val component = message.toColor.toComponent
        to.forEach {
            it.sendMessage(component)
        }
    }

    /**
     * メッセージを複数のプレイヤーに送信します
     * @param message 本文
     * @param to 送信先
     */
    fun send(
        message: TextComponent,
        vararg to: CommandSender
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
     * タイトルを表示します
     * @param main メインタイトル
     * @param sub サブタイトル
     * @param fadeIn フェードイン時間
     * @param stay 表示時間
     * @param fadeOut フェードアウト時間
     */
    fun ProxiedPlayer.title(
        main: String,
        sub: String,
        fadeIn: Int,
        stay: Int,
        fadeOut: Int
    ) {
        sendTitle(
            plugin.proxy.createTitle().apply {
                title(main.toColor.toComponent)
                subTitle(sub.toColor.toComponent)
                fadeIn(fadeIn)
                stay(stay)
                fadeOut(fadeOut)
            }
        )
    }

    /**
     * アクションバーにメッセージを表示します
     * @param message 本文
     */
    fun ProxiedPlayer.action(message: String) {
        sendMessage(ChatMessageType.ACTION_BAR, message.toColor.toComponent)
    }
}
